package com.ahmetocak.movieapp.data.datasource.remote.firebase.auth

import com.ahmetocak.movieapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {
    override fun signUp(auth: Auth): Task<AuthResult> =
        firebaseAuth.createUserWithEmailAndPassword(auth.email, auth.password)

    override fun login(auth: Auth): Task<AuthResult> =
        firebaseAuth.signInWithEmailAndPassword(auth.email, auth.password)

    override fun sendResetPasswordEmail(email: String): Task<Void> =
        firebaseAuth.sendPasswordResetEmail(email)

    override fun reAuthenticate(auth: Auth): Task<Void>? {
        return firebaseAuth.currentUser?.reauthenticate(
            EmailAuthProvider.getCredential(
                auth.email,
                auth.password
            )
        )
    }

    override fun deleteAccount(): Task<Void>? = firebaseAuth.currentUser?.delete()
}