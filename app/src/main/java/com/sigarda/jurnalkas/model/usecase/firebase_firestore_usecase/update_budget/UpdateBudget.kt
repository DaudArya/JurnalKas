package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.update_budget

import com.google.android.gms.tasks.Task
import java.util.*

interface UpdateBudget {
    suspend operator fun invoke(
        amount: Float,
        title : String,
        isIncome: Boolean,
        type: String,
        date: Date,
        currentUserId: String,
        documentId: String
    ): Task<Void>
}