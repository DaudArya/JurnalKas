package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.save_budget

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import com.sigarda.jurnalkas.utils.Constant
import java.util.Date
import javax.inject.Inject

class SaveBudgetImpl @Inject constructor(
    private val firestore: FirebaseFirestoreDatabase
) : SaveBudget {

    override suspend operator fun invoke(
        amount: Float,
        isIncome: Boolean,
        type: String,
        date: Date,
        currentUserId: String
    ): Task<DocumentReference> {

        val hashMap = hashMapOf<String, Any>(
            Constant.AMOUNT to amount,
            Constant.IS_INCOME to isIncome,
            Constant.TYPE to type,
            Constant.DATE to date.time,
            Constant.USER_ID to currentUserId
        )
        return firestore.saveBudget(hashMap = hashMap)

    }
}