package com.sigarda.jurnalkas.model

import java.util.*

data class TransactionUiModel(
    val amount: Float,
    val isIncome: Boolean,
    val type: String,
    val date: Date,
    val icon: Int,
    val textColor: Int,
    val strokeColor: Int,
    val chartColor: String,
    val cardColor: String,
    val id: String?
)
