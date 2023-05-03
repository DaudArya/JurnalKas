package com.sigarda.jurnalkas.data.network.model.profile.updateprofile


import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?
)