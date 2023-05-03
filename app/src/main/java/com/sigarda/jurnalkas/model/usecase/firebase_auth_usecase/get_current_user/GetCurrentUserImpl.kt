package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.get_current_user

import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import javax.inject.Inject

class GetCurrentUserImpl @Inject constructor(
    private val auth: FirebaseAuthenticator
) : GetCurrentUser {

    override suspend operator fun invoke(): Boolean {
        return auth.getCurrentUser()
    }
}