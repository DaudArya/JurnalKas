package com.sigarda.jurnalkas.data.repository.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import javax.inject.Inject

class FirebaseAuthenticatorImpl @Inject constructor(
    private val auth : FirebaseAuth
): FirebaseAuthenticator {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
        return  auth.signInWithEmailAndPassword(email, password)
    }
    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
       return auth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser() : Boolean {
        return auth.currentUser != null
    }

    override suspend fun getCurrentUserInfo(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun forgotPassword(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }
}