package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.save_user

import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import javax.inject.Inject

class SaveUserImpl @Inject constructor(
    private val firestore: FirebaseFirestoreDatabase
) : SaveUser {

    override suspend operator fun invoke(
        getFirebaseUserUid: String,
        email: String,
        name: String,
        password: String
    ) {
        firestore.saveUser(
            getFirebaseUserUid = getFirebaseUserUid,
            email = email,
            name = name,
            password = password
        )

    }
}