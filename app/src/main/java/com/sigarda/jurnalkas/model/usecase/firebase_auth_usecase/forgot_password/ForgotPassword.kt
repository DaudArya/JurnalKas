package com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.forgot_password

import com.google.android.gms.tasks.Task

interface ForgotPassword {
    suspend operator fun invoke(email: String): Task<Void>
}