package com.sigarda.jurnalkas.data.network.model.transaction.wallet.requestbody


import com.google.gson.annotations.SerializedName

data class WalletRequestBody(
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("status")
    val status: String?
)