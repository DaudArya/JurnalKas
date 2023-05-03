package com.sigarda.jurnalkas.data.network.model.transaction.postcategories.requestbody

import com.google.gson.annotations.SerializedName

data class CategoriesRequestBody(
    @SerializedName("name")
    val name: String? = null
)
