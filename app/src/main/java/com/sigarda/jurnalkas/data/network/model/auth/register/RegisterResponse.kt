package com.sigarda.jurnalkas.data.network.model.auth.register


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("response")
    val response: Response?
)