package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.get_current_user

interface GetCurrentUser {
    suspend operator fun invoke(): Boolean
}