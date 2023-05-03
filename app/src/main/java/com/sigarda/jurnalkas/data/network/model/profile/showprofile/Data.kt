package com.sigarda.jurnalkas.data.network.model.profile.showprofile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Data(
    @SerializedName("apps")
    val apps: List<String?>?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("licenses")
    val licenses: List<String?>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("uuid")
    val uuid: String?
) : Parcelable