package com.sigarda.jurnalkas.data.network.model.transaction.getcategories.response


import com.google.gson.annotations.SerializedName

data class GetCategoriesResponse(
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("response")
    val response: Response?
)