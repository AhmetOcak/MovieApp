package com.ahmetocak.network.datasource.firebase.auth

import com.ahmetocak.network.model.firebase.auth.NetworkAuth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface FirebaseAuthDataSource {

    fun signUp(auth: NetworkAuth): Task<AuthResult>

    fun signOut()

    fun login(auth: NetworkAuth): Task<AuthResult>

    fun sendResetPasswordEmail(email: String): Task<Void>

    fun reAuthenticate(auth: NetworkAuth): Task<Void>?

    fun deleteAccount(): Task<Void>?

    fun getUserEmail(): String?
}