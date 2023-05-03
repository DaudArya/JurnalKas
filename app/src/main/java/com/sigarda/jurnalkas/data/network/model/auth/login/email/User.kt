package com.sigarda.jurnalkas.data.network.model.auth.login.email


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: Any?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("username")
    val username: Any?,
    @SerializedName("uuid")
    val uuid: String?
)