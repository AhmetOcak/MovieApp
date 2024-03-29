package com.ahmetocak.login

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.common.helpers.DialogUiEvent
import com.ahmetocak.designsystem.components.ButtonCircularProgressIndicator
import com.ahmetocak.designsystem.components.MovieButton
import com.ahmetocak.designsystem.components.MovieDialog
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.components.MovieTextButton
import com.ahmetocak.designsystem.components.auth.AuthBackground
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthWelcomeMessage
import com.ahmetocak.designsystem.dimens.ComponentDimens
import com.ahmetocak.designsystem.dimens.Dimens

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onCreateAccountClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    viewModel.signInWithGoogle(
                        intent = intent,
                        navigateToHome = onNavigateToHome
                    )
                }
            }
        }
    )

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
            onDismissRequest = remember { viewModel::endResetPasswordDialog },
            onSendClick = remember { viewModel::sendPasswordResetMail },
            isLoading = uiState.dialogUiEvent == DialogUiEvent.Loading,
            emailLabelText = uiState.passwordResetFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.email_address_text),
            emailValue = viewModel.passwordResetEmailValue,
            onEmailValueChange = remember { viewModel::updatePasswordResetMail },
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
                onEmailValueChange = remember { viewModel::updateEmailValue },
                emailFieldError = uiState.emailFieldErrorMessage != null,
                emailFieldLabel = uiState.emailFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.email_label),
                passwordValue = viewModel.passwordValue,
                onPasswordValueChange = remember { viewModel::updatePasswordValue },
                passwordFieldError = uiState.passwordFieldErrorMessage != null,
                passwordFieldLabel = uiState.passwordFieldErrorMessage?.asString()
                    ?: stringResource(id = R.string.password_label),
                onLoginClick = remember { { viewModel.login(onNavigateToHome) } },
                onCreateAccountClick = onCreateAccountClick,
                onForgotPasswordClick = remember { viewModel::startResetPasswordDialog },
                onGoogleSignInClick = remember { {
                    viewModel.startSignInWithGoogleIntent { intentSenderRequest ->
                        launcher.launch(intentSenderRequest)
                    }
                } }
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
    onForgotPasswordClick: () -> Unit,
    onGoogleSignInClick: () -> Unit
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
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            MovieTextButton(
                text = stringResource(id = R.string.forgot_password_text),
                onClick = onForgotPasswordClick
            )
        }
        MovieButton(
            modifier = Modifier
                .padding(top = Dimens.oneLevelPadding)
                .height(ComponentDimens.buttonHeight)
                .fillMaxWidth(),
            text = stringResource(id = R.string.login_button_text),
            onClick = onLoginClick
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.primary)
            Text(
                text = stringResource(id = R.string.or_contiune_with),
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.primary)
        }
        GoogleSignInButton(onClick = onGoogleSignInClick)
        SignUpNow(onCreateAccountClick = onCreateAccountClick)
    }
}

@Composable
private fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.size(48.dp),
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = null
        )
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