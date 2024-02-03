package com.ahmetocak.movieapp.presentation.signup

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieButton
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieScaffold
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthBackground
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthEmailOutlinedTextField
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthWelcomeMessage
import com.ahmetocak.movieapp.utils.ComponentDimens
import com.ahmetocak.movieapp.utils.Dimens

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessage()
    }

    MovieScaffold(modifier = modifier) { paddingValues ->
        AuthBackground()
        if (uiState.isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            SignUpScreenContent(
                modifier = Modifier.padding(paddingValues),
                emailValue = viewModel.emailValue,
                onEmailValueChange = remember(viewModel) { viewModel::updateEmailValue },
                emailFieldError = uiState.emailFieldErrorMessage != null,
                emailFieldLabel = uiState.emailFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.email_label),
                passwordValue = viewModel.passwordValue,
                onPasswordValueChange = remember(viewModel) { viewModel::updatePasswordValue },
                passwordFieldError = uiState.passwordFieldErrorMessage != null,
                passwordFieldLabel = uiState.passwordFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.password_label),
                confirmPasswordValue = viewModel.confirmPasswordValue,
                onConfirmPasswordValueChange = remember(viewModel) { viewModel::updateConfirmPasswordValue },
                confirmPasswordFieldError = uiState.confirmPasswordFieldErrorMessage != null,
                confirmPasswordLabel = uiState.confirmPasswordFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.password_confirm_label),
                onSignUpClick = remember(viewModel) { { viewModel.signUp(onSignUpClick) } }
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = Dimens.eightLevelPadding)
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        AuthWelcomeMessage(text = stringResource(id = R.string.sign_up_welcome_message))
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