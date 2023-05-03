package com.sigarda.jurnalkas.data.network.model.transaction.wallet.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Long?
)