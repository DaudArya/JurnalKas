package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.save_user

interface SaveUser {
    suspend operator fun invoke(
        getFirebaseUserUid: String,
        email: String,
        name: String,
        password: String
    )
}