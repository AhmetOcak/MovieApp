package com.ahmetocak.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.DynamicLayout
import com.ahmetocak.designsystem.components.MovieButton
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.components.auth.AuthBackground
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthWelcomeMessage
import com.ahmetocak.designsystem.dimens.ComponentDimens
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.designsystem.dimens.setAdaptiveWidth

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessage()
    }

    MovieScaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        AuthBackground()
        if (uiState.isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            SignUpScreenContent(
                modifier = Modifier.padding(paddingValues),
                emailValue = viewModel.emailValue,
                onEmailValueChange = remember { viewModel::updateEmailValue },
                emailFieldError = uiState.emailFieldErrorMessage != null,
                emailFieldLabel = uiState.emailFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.email_label),
                passwordValue = viewModel.passwordValue,
                onPasswordValueChange = remember { viewModel::updatePasswordValue },
                passwordFieldError = uiState.passwordFieldErrorMessage != null,
                passwordFieldLabel = uiState.passwordFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.password_label),
                confirmPasswordValue = viewModel.confirmPasswordValue,
                onConfirmPasswordValueChange = remember { viewModel::updateConfirmPasswordValue },
                confirmPasswordFieldError = uiState.confirmPasswordFieldErrorMessage != null,
                confirmPasswordLabel = uiState.confirmPasswordFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.password_confirm_label),
                onSignUpClick = remember { { viewModel.signUp(onSignUpClick) } }
            )
        }
    }
}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    emailFieldLabel: String,
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordFieldError: Boolean,
    passwordFieldLabel: String,
    confirmPasswordValue: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    confirmPasswordFieldError: Boolean,
    confirmPasswordLabel: String,
    onSignUpClick: () -> Unit
) {
    DynamicLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AuthWelcomeMessage(text = stringResource(id = R.string.sign_up_welcome_message))
        AuthSection(
            emailValue = emailValue,
            onEmailValueChange = onEmailValueChange,
            emailFieldError = emailFieldError,
            emailFieldLabel = emailFieldLabel,
            passwordValue = passwordValue,
            onPasswordValueChange = onPasswordValueChange,
            passwordFieldError = passwordFieldError,
            passwordFieldLabel = passwordFieldLabel,
            confirmPasswordValue = confirmPasswordValue,
            onConfirmPasswordValueChange = onConfirmPasswordValueChange,
            confirmPasswordFieldError = confirmPasswordFieldError,
            confirmPasswordLabel = confirmPasswordLabel,
            onSignUpClick = onSignUpClick
        )
    }
}


@Composable
private fun AuthSection(
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    emailFieldLabel: String,
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordFieldError: Boolean,
    passwordFieldLabel: String,
    confirmPasswordValue: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    confirmPasswordFieldError: Boolean,
    confirmPasswordLabel: String,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier.setAdaptiveWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthEmailOutlinedTextField(
            value = emailValue,
            onValueChange = onEmailValueChange,
            isError = emailFieldError,
            labelText = emailFieldLabel
        )
        AuthPasswordOutlinedTextField(
            value = passwordValue,
            onValueChange = onPasswordValueChange,
            isError = passwordFieldError,
            labelText = passwordFieldLabel
        )
        AuthPasswordOutlinedTextField(
            value = confirmPasswordValue,
            onValueChange = onConfirmPasswordValueChange,
            isError = confirmPasswordFieldError,
            labelText = confirmPasswordLabel
        )
        MovieButton(
            modifier = Modifier
                .padding(top = Dimens.twoLevelPadding)
                .height(ComponentDimens.buttonHeight)
                .fillMaxWidth(),
            text = stringResource(id = R.string.sign_up_button_text),
            onClick = onSignUpClick
        )
    }
}