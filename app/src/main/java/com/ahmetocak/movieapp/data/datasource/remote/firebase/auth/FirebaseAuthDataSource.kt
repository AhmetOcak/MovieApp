package com.ahmetocak.movieapp.data.datasource.remote.firebase.auth

import com.ahmetocak.movieapp.model.firebase.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface FirebaseAuthDataSource {

    fun signUp(auth: Auth): Task<AuthResult>

    fun login(auth: Auth): Task<AuthResult>

    fun sendResetPasswordEmail(email: String): Task<Void>

    fun reAuthenticate(auth: Auth): Task<Void>?

    fun deleteAccount(): Task<Void>?
}