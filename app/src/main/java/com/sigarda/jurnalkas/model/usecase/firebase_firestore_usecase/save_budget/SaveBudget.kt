package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.save_budget

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import java.util.*

interface SaveBudget {
    suspend operator fun invoke(amount: Float,
                                title : String,
                                isIncome: Boolean,
                                type: String,
                                date: Date,
                                currentUserId: String
    ): Task<DocumentReference>
}