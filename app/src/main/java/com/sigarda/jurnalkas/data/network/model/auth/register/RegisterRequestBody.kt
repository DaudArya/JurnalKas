package com.sigarda.jurnalkas.data.network.model.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody (
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null
)