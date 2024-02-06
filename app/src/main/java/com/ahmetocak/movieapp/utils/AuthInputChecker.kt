package com.ahmetocak.movieapp.utils

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText

/**
 * Object containing utility functions for checking input fields related to sign-up operations.
 */
object AuthInputChecker {

    /**
     * Checks the validity of the email field.
     *
     * @param email The email input to be validated.
     * @param onBlank Callback to be executed if the email is blank.
     * @param onUnValid Callback to be executed if the email is not a valid email format.
     * @param onSuccess Callback to be executed if the email is valid.
     * @return `true` if the email is valid, `false` otherwise.
     */
    fun checkEmailField(
        email: String,
        onBlank: (UiText) -> Unit,
        onUnValid: (UiText) -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (email.isBlank()) {
            onBlank(UiText.StringResource(R.string.blank_field))
            false
        } else if (!email.isValidEmail()) {
            onUnValid(UiText.StringResource(R.string.unvalid_email))
            false
        } else {
            onSuccess()
            true
        }
    }

    /**
     * Checks the validity of the password field.
     *
     * @param password The password input to be validated.
     * @param onBlank Callback to be executed if the password is blank.
     * @param onUnValid Callback to be executed if the password is less than 6 characters.
     * @param onSuccess Callback to be executed if the password is valid.
     * @return `true` if the password is valid, `false` otherwise.
     */
    fun checkPasswordField(
        password: String,
        onBlank: (UiText) -> Unit,
        onUnValid: (UiText) -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (password.isBlank()) {
            onBlank(UiText.StringResource(R.string.blank_field))
            false
        } else if (password.length < 6) {
            onUnValid(UiText.StringResource(R.string.password_length))
            false
        } else {
            onSuccess()
            true
        }
    }

    /**
     * Checks the validity of the confirm password field.
     *
     * @param confirmPassword The confirm password input to be validated.
     * @param password The original password input.
     * @param onBlank Callback to be executed if the confirm password is blank.
     * @param onUnValid Callback to be executed if the confirm password does not match the original password.
     * @param onSuccess Callback to be executed if the confirm password is valid.
     * @return `true` if the confirm password is valid, `false` otherwise.
     */
    fun checkConfirmPasswordField(
        confirmPassword: String,
        password: String,
        onBlank: (UiText) -> Unit,
        onUnValid: (UiText) -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (password.isBlank()) {
            onBlank(UiText.StringResource(R.string.blank_field))
            false
        } else if (confirmPassword != password) {
            onUnValid(UiText.StringResource(R.string.passwords_dont_match))
            false
        } else {
            onSuccess()
            true
        }
    }
}