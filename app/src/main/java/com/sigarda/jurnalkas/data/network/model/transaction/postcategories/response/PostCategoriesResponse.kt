package com.sigarda.jurnalkas.data.network.model.transaction.postcategories.response


import com.google.gson.annotations.SerializedName

data class PostCategoriesResponse(
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("response")
    val response: Response?
)