package com.ahmetocak.movieapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.MovieButton
import com.ahmetocak.movieapp.presentation.ui.components.MovieDialog
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.components.MovieTextButton
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthBackground
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthWelcomeMessage
import com.ahmetocak.movieapp.presentation.ui.theme.MovieAppTheme
import com.ahmetocak.movieapp.utils.ComponentDimens
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.ScreenPreview

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier, onCreateAccountClick: () -> Unit, onLoginClick: () -> Unit
) {

    MovieScaffold(modifier = modifier) { paddingValues ->
        AuthBackground()
        LoginScreenContent(
            modifier = Modifier.padding(paddingValues),
            emailValue = "",
            onEmailValueChange = {},
            emailFieldError = false,
            emailFieldLabel = stringResource(id = R.string.email_label),
            passwordValue = "",
            onPasswordValueChange = {},
            passwordFieldError = false,
            passwordFieldLabel = stringResource(id = R.string.password_label),
            onLoginClick = onLoginClick,
            onCheckedChange = {},
            onCreateAccountClick = onCreateAccountClick,
            onForgotPasswordClick = {},
            rememberMeValue = false
        )
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
    onCheckedChange: (Boolean) -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    rememberMeValue: Boolean
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
        RememberMeBox(checked = rememberMeValue, onCheckedChange = onCheckedChange)
        MovieButton(
            modifier = Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RememberMeBox(
    checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.twoLevelPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                checked = checked, onCheckedChange = onCheckedChange
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.remember_me_text))
    }
}

@Composable
private fun ForgotPasswordDialog(
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onSendClick: () -> Unit
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
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(text = stringResource(id = R.string.email_address_text))
                }
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ElevatedButton(onClick = onCancelClick) {
                    Text(text = stringResource(id = R.string.cancel_text))
                }
                Spacer(modifier = Modifier.width(Dimens.twoLevelPadding))
                ElevatedButton(onClick = onSendClick) {
                    Text(text = stringResource(id = R.string.send_text))
                }
            }
        }
    }
}

@ScreenPreview
@Composable
private fun LoginScreenPreview() {
    MovieAppTheme {
        Surface {
            LoginScreen(onCreateAccountClick = {}, onLoginClick = {})
        }
    }
}