package com.ahmetocak.movieapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.DialogUiEvent
import com.ahmetocak.movieapp.presentation.components.designsystem.ButtonCircularProgressIndicator
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieButton
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieDialog
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieScaffold
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieTextButton
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthBackground
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthEmailOutlinedTextField
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.movieapp.presentation.components.designsystem.auth.AuthWelcomeMessage
import com.ahmetocak.movieapp.utils.ComponentDimens
import com.ahmetocak.movieapp.utils.Dimens

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
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

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedUserMessage()
    }

    if (uiState.dialogUiEvent != DialogUiEvent.InActive) {
        ForgotPasswordDialog(
            onDismissRequest = remember(viewModel) { viewModel::endResetPasswordDialog },
            onSendClick = remember(viewModel) { viewModel::sendPasswordResetMail },
            isLoading = uiState.dialogUiEvent == DialogUiEvent.Loading,
            emailLabelText = uiState.passwordResetFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.email_address_text),
            emailValue = viewModel.passwordResetEmailValue,
            onEmailValueChange = remember(viewModel) { viewModel::updatePasswordResetMail },
            isEmailFieldError = uiState.passwordResetFieldErrorMessage != null
        )
    }

    MovieScaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        AuthBackground()
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LoginScreenContent(
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
                onLoginClick = remember(viewModel) { { viewModel.login(onLoginClick) } },
                onCreateAccountClick = onCreateAccountClick,
                onForgotPasswordClick = remember(viewModel) { viewModel::startResetPasswordDialog }
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    emailFieldLabel: String,
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordFieldError: Boolean,
    passwordFieldLabel: String,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = Dimens.eightLevelPadding)
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        AuthWelcomeMessage(text = stringResource(id = R.string.login_welcome_message))
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
        MovieButton(
            modifier = Modifier
                .padding(top = Dimens.twoLevelPadding)
                .height(ComponentDimens.buttonHeight)
                .fillMaxWidth(),
            text = stringResource(id = R.string.login_button_text),
            onClick = onLoginClick
        )
        MovieTextButton(
            text = stringResource(id = R.string.forgot_password_text),
            onClick = onForgotPasswordClick
        )
        SignUpNow(onCreateAccountClick = onCreateAccountClick)
    }
}

@Composable
private fun SignUpNow(onCreateAccountClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.no_account_text))
        Spacer(modifier = Modifier.width(4.dp))
        MovieTextButton(
            text = stringResource(id = R.string.sign_up_button_text),
            onClick = onCreateAccountClick,
            fontWeight = FontWeight.Bold,
            contentPadding = PaddingValues(0.dp)
        )
    }
}

@Composable
private fun ForgotPasswordDialog(
    onDismissRequest: () -> Unit,
    onSendClick: () -> Unit,
    isLoading: Boolean,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailLabelText: String,
    isEmailFieldError: Boolean
) {
    MovieDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.threeLevelPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            Text(
                text = stringResource(id = R.string.password_reset_text),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = stringResource(id = R.string.password_reset_description_text))
            TextField(
                value = emailValue,
                onValueChange = onEmailValueChange,
                label = {
                    Text(text = emailLabelText)
                },
                isError = isEmailFieldError
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ElevatedButton(onClick = onDismissRequest) {
                    Text(text = stringResource(id = R.string.cancel_text))
                }
                Spacer(modifier = Modifier.width(Dimens.twoLevelPadding))
                ElevatedButton(
                    enabled = !isLoading && emailValue.isNotBlank(),
                    onClick = onSendClick
                ) {
                    if (isLoading) {
                        ButtonCircularProgressIndicator()
                    } else {
                        Text(text = stringResource(id = R.string.send_text))
                    }
                }
            }
        }
    }
}