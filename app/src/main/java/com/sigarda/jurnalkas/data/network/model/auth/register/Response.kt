package com.sigarda.jurnalkas.data.network.model.auth.register


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("token")
    val token: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("user")
    val user: User?
)