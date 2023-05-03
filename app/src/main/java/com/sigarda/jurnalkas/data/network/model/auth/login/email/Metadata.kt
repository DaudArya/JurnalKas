package com.sigarda.jurnalkas.data.network.model.auth.login.email


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?
)