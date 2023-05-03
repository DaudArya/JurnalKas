package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.get_budget_from_firestore

import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.wrapper.Resource

interface GetBudgetFromFirestore {
    suspend operator fun invoke(): Resource<List<TransactionUiModel>>
}