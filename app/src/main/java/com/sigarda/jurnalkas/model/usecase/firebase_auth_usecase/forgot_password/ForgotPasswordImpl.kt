package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.forgot_password

import com.google.android.gms.tasks.Task
import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import javax.inject.Inject

class ForgotPasswordImpl @Inject constructor(
    private val auth: FirebaseAuthenticator
) : ForgotPassword {
    override suspend operator fun invoke(email: String): Task<Void> {
        return auth.forgotPassword(email)
    }
}