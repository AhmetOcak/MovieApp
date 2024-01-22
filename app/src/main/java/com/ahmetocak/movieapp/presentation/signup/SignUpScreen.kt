package com.ahmetocak.movieapp.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.MovieButton
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthBackground
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthWelcomeMessage
import com.ahmetocak.movieapp.presentation.ui.theme.MovieAppTheme
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.ScreenPreview

@Composable
fun SignUpScreen(modifier: Modifier = Modifier, upPress: () -> Unit) {

    MovieScaffold(modifier = modifier) { paddingValues ->
        AuthBackground()
        SignUpScreenContent(
            modifier = Modifier.padding(paddingValues),
            emailValue = "",
            onEmailValueChange = {},
            emailFieldError = false,
            emailFieldLabel = stringResource(id = R.string.email_label),
            passwordValue = "",
            onPasswordValueChange = {},
            passwordFieldError = false,
            passwordFieldLabel = stringResource(id = R.string.password_label),
            confirmPasswordValue = "",
            onConfirmPasswordValueChange = {},
            confirmPasswordFieldError = false,
            confirmPasswordLabel = stringResource(id = R.string.password_confirm_label),
            onSignUpClick = { upPress() }
        )
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
                .fillMaxWidth()
                .padding(top = Dimens.oneLevelPadding),
            text = stringResource(id = R.string.sign_up_button_text),
            onClick = onSignUpClick
        )
    }
}

@ScreenPreview
@Composable
private fun SignUpScreenPreview() {
    MovieAppTheme {
        Surface {
            SignUpScreen(upPress = {})
        }
    }
}