package com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.get_budget_from_firestore

import com.sigarda.jurnalkas.data.local.entity.Transaction
import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.utils.Constant
import com.sigarda.jurnalkas.wrapper.Resource
import com.sigarda.jurnalkas.wrapper.TransactionMapper
import java.util.Date
import javax.inject.Inject

class GetBudgetFromFirestoreImpl @Inject constructor(
    private val firestore: FirebaseFirestoreDatabase,
    private val auth: FirebaseAuthenticator,
    private val mapper: TransactionMapper
): GetBudgetFromFirestore {

    override suspend operator fun invoke(): Resource<List<TransactionUiModel>> {
        val userId = auth.getCurrentUserInfo()!!.uid
        return try {
            Resource.Loading(data = null)
            val result = firestore.getBudgetDocuments(userId = userId)
            val budgetList = ArrayList<TransactionUiModel>()
            budgetList.clear()
            result.forEach {
                val budget = Transaction(
                    amount = it.get(Constant.AMOUNT).toString().toFloat(),
                    title = it.get(Constant.TITLE).toString(),
                    isIncome = it.get(Constant.IS_INCOME).toString().toBoolean(),
                    type = it.get(Constant.TYPE).toString(),
                    date = Date(it.get(Constant.DATE).toString().toLong()),
                    id = it.id
                ).run {
                    mapper.map(this)
                }
                budgetList.add(budget)
            }
            Resource.Success(budgetList)
        } catch (e: Exception) {
            Resource.Error(e, e.message)
        }
    }
}
