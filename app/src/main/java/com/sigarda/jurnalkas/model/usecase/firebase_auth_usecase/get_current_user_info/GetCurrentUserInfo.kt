package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.get_current_user_info

import com.google.firebase.auth.FirebaseUser

interface GetCurrentUserInfo {
    suspend operator fun invoke(): FirebaseUser?
}