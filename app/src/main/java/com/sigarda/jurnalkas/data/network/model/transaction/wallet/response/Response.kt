package com.sigarda.jurnalkas.data.network.model.transaction.wallet.response


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("data")
    val `data`: Data?
)