package com.ahmetocak.movieapp.utils

/**
 * Object containing utility functions for checking input fields related to sign-up operations.
 */
object SignUpInputChecker {

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
        onBlank: () -> Unit,
        onUnValid: () -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (email.isBlank()) {
            onBlank()
            false
        } else if (!email.isValidEmail()) {
            onUnValid()
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
        onBlank: () -> Unit,
        onUnValid: () -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (password.isBlank()) {
            onBlank()
            false
        } else if (password.length < 6) {
            onUnValid()
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
        onBlank: () -> Unit,
        onUnValid: () -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (password.isBlank()) {
            onBlank()
            false
        } else if (confirmPassword != password) {
            onUnValid()
            false
        } else {
            onSuccess()
            true
        }
    }
}

/**
 * Object containing utility functions for checking input fields related to login operations.
 */
object LoginInputChecker {

    /**
     * Checks the validity of the email field for login.
     *
     * @param email The email input to be validated.
     * @param onBlank Callback to be executed if the email is blank.
     * @param onSuccess Callback to be executed if the email is valid.
     * @return `true` if the email is valid, `false` otherwise.
     */
    fun checkEmailField(
        email: String,
        onBlank: () -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (email.isBlank()) {
            onBlank()
            false
        } else {
            onSuccess()
            true
        }
    }

    /**
     * Checks the validity of the password field for login.
     *
     * @param password The password input to be validated.
     * @param onBlank Callback to be executed if the password is blank.
     * @param onSuccess Callback to be executed if the password is valid.
     * @return `true` if the password is valid, `false` otherwise.
     */
    fun checkPasswordField(
        password: String,
        onBlank: () -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (password.isBlank()) {
            onBlank()
            false
        } else {
            onSuccess()
            true
        }
    }
}