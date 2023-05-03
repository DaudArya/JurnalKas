package com.sigarda.jurnalkas.data.network.model.auth.login.email


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("response")
    val response: Response?
)