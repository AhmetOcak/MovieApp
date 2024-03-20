package com.ahmetocak.authentication.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthClient @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    fun signOut() = auth.signOut()

    fun login(email: String, password: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    fun sendResetPasswordEmail(email: String): Task<Void> =
        auth.sendPasswordResetEmail(email)

    fun reAuthenticate(email: String, password: String): Task<Void>? {
        return auth.currentUser?.reauthenticate(
            EmailAuthProvider.getCredential(email, password)
        )
    }

    fun deleteAccount(): Task<Void>? = auth.currentUser?.delete()

    fun getUserEmail(): String? = auth.currentUser?.email
}