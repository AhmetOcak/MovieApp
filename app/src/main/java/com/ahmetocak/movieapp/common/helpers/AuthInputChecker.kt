package com.ahmetocak.movieapp.common.helpers

import android.util.Patterns.EMAIL_ADDRESS

fun String.isValidEmail(): Boolean = EMAIL_ADDRESS.matcher(this).matches()

object SignUpInputChecker {

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

object LoginInputChecker {
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