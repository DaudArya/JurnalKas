package com.sigarda.jurnalkas.data.repository

import com.sigarda.jurnalkas.data.local.database.AppDatabase
import com.sigarda.jurnalkas.data.local.entity.TransactionEntity
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val db: AppDatabase) {

    // insert transaction
    suspend fun insert(transaction: TransactionEntity) = db.transactionDao().insertTransaction(
        transaction
    )

    // update transaction
    suspend fun update(transaction: TransactionEntity) = db.transactionDao().updateTransaction(
        transaction
    )

    // delete transaction
    suspend fun delete(transaction: TransactionEntity) = db.transactionDao().deleteTransaction(
        transaction
    )

    fun deleteById(id:Int){
        db.transactionDao().deleteById(id)

    }

    // get all transaction
    fun getAllTransactions() = db.transactionDao().getAllTransactions()

    // get single transaction type - Expense or Income or else overall
    fun getAllSingleTransaction(transactionType: String) = if (transactionType == "Overall") {
        getAllTransactions()
    } else {
        db.transactionDao().getAllSingleTransaction(transactionType)
    }

    // get transaction by ID
    fun getByID(id: Int) = db.transactionDao().getTransactionByID(id)

    // delete transaction by ID
    suspend fun deleteByID(id: Int) = db.transactionDao().deleteTransactionByID(id)
}