package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.delete_budget

import com.google.android.gms.tasks.Task

interface DeleteBudget {
    suspend operator fun invoke(documentId: String): Task<Void>
}