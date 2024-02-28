package com.ahmetocak.network.datasource.firebase.auth

import com.ahmetocak.network.model.firebase.auth.NetworkAuth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {
    override fun signUp(auth: NetworkAuth): Task<AuthResult> =
        firebaseAuth.createUserWithEmailAndPassword(auth.email, auth.password)

    override fun signOut() = firebaseAuth.signOut()

    override fun login(auth: NetworkAuth): Task<AuthResult> =
        firebaseAuth.signInWithEmailAndPassword(auth.email, auth.password)

    override fun sendResetPasswordEmail(email: String): Task<Void> =
        firebaseAuth.sendPasswordResetEmail(email)

    override fun reAuthenticate(auth: NetworkAuth): Task<Void>? {
        return firebaseAuth.currentUser?.reauthenticate(
            EmailAuthProvider.getCredential(
                auth.email,
                auth.password
            )
        )
    }

    override fun deleteAccount(): Task<Void>? = firebaseAuth.currentUser?.delete()

    override fun getUserEmail(): String? = firebaseAuth.currentUser?.email
}