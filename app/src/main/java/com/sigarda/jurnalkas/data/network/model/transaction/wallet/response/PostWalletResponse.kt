package com.sigarda.jurnalkas.data.network.model.transaction.wallet.response


import com.google.gson.annotations.SerializedName

data class PostWalletResponse(
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("response")
    val response: Response?
)