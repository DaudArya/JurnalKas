package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.update_budget

import com.google.android.gms.tasks.Task
import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import com.sigarda.jurnalkas.utils.Constant
import java.util.Date
import javax.inject.Inject

class UpdateBudgetImpl @Inject constructor(
    private val firestore: FirebaseFirestoreDatabase
) : UpdateBudget{

    override suspend operator fun invoke(
        amount: Float,
        title : String,
        isIncome: Boolean,
        type: String,
        date: Date,
        currentUserId: String,
        documentId: String
    ): Task<Void> {

        val hashMap = hashMapOf<String, Any>(
            Constant.TITLE to title,
            Constant.AMOUNT to amount,
            Constant.IS_INCOME to isIncome,
            Constant.TYPE to type,
            Constant.DATE to date.time,
            Constant.USER_ID to currentUserId
        )
        return firestore.updateBudget(hashMap = hashMap, documentId = documentId)
    }

}







