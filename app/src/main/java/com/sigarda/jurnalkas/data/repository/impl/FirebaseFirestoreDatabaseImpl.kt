package com.sigarda.jurnalkas.data.repository.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import com.sigarda.jurnalkas.utils.Constant
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreDatabaseImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseFirestoreDatabase {


    override suspend fun saveUser(
        getFirebaseUserUid: String,
        email: String,
        name: String,
        password: String
    ) {

        val user = hashMapOf<String, Any>(
            Constant.ID to getFirebaseUserUid,
            Constant.E_MAIL to email,
            Constant.NICKNAME to name,
            Constant.PASSWORD to password
        )
        firestore.collection(Constant.COLLECTION_PATH_USER)
            .document(getFirebaseUserUid)
            .set(user).await()
    }

    override suspend fun saveBudget(hashMap: HashMap<String, Any>): Task<DocumentReference>{
        return firestore.collection(Constant.COLLECTION_PATH_BUDGET).add(hashMap)
    }

    override suspend fun getBudgetDocuments(userId: String): QuerySnapshot {
        return firestore.collection(Constant.COLLECTION_PATH_BUDGET)
            .whereEqualTo(Constant.USER_ID, userId).get().await()
    }

    override suspend fun deleteBudget(documentId: String): Task<Void> {
        return firestore.collection(Constant.COLLECTION_PATH_BUDGET).document(documentId).delete()
    }

    override suspend fun updateBudget(
        hashMap: HashMap<String, Any>,
        documentId: String
    ): Task<Void> {
        return firestore.collection(Constant.COLLECTION_PATH_BUDGET).document(documentId)
            .update(hashMap)
    }
}