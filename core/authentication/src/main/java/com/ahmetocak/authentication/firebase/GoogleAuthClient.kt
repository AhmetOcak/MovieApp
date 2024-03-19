package com.ahmetocak.authentication.firebase

import android.content.Context
import android.content.Intent
import com.ahmetocak.authentication.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class GoogleAuthClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient
) {

    fun startSignInIntent(): Task<BeginSignInResult> = oneTapClient.beginSignIn(signInRequest())

    fun signInWithIntent(intent: Intent): Task<AuthResult> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val idToken = credential.googleIdToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(idToken, null)

        return Firebase.auth.signInWithCredential(googleAuthCredential)
    }

    fun reAuthenticate(): Task<GoogleSignInAccount> {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_auth_web_client_id))
            .build()

        val googleClient = GoogleSignIn.getClient(context, googleSignInOptions)

        return googleClient.silentSignIn()
    }

    private fun signInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.google_auth_web_client_id))
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()
}