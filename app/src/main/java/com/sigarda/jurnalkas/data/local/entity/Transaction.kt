package com.sigarda.jurnalkas.data.local.entity

import java.util.Date

data class Transaction(
    val id: String? = null,
    val amount: Float,
    val isIncome: Boolean,
    val type: String,
    val date: Date
)
