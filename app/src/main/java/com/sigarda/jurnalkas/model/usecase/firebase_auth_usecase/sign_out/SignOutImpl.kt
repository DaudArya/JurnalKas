package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.sign_out

import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import javax.inject.Inject

class SignOutImpl @Inject constructor(
    private val auth: FirebaseAuthenticator
): SignOut {

    override suspend operator fun invoke() {
        auth.signOut()
    }
}