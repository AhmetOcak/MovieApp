package com.ahmetocak.movieapp.common.helpers

import android.util.Patterns.EMAIL_ADDRESS

object AuthInputChecker {

    fun checkEmailField(
        email: String,
        onBlank: () -> Unit,
        onUnValid: () -> Unit,
        onSuccess: () -> Unit
    ): Boolean {
        return if (email.isBlank()) {
            onBlank()
            false
        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            onUnValid()
            false
        } else {
            onSuccess()
            true
        }
    }

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