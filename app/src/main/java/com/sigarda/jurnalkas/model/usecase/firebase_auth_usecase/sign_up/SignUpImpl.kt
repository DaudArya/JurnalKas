package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.sign_up

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import javax.inject.Inject

class SignUpImpl @Inject constructor(
    private val auth: FirebaseAuthenticator
): SignUp{
    override suspend operator fun invoke(email: String,password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email = email, password = password)
    }
}