package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.get_current_user_info

import com.google.firebase.auth.FirebaseUser
import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import javax.inject.Inject

class GetCurrentUserInfoImpl @Inject constructor(
    private val auth: FirebaseAuthenticator
): GetCurrentUserInfo {

    override suspend operator fun invoke(): FirebaseUser? {
        return auth.getCurrentUserInfo()
    }
}