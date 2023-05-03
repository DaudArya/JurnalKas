package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.delete_budget

import com.google.android.gms.tasks.Task
import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import javax.inject.Inject

class DeleteBudgetImpl @Inject constructor(
    private val firestore: FirebaseFirestoreDatabase
) : DeleteBudget {

    override suspend operator fun invoke(documentId: String): Task<Void> {
        return firestore.deleteBudget(documentId = documentId)
    }
}