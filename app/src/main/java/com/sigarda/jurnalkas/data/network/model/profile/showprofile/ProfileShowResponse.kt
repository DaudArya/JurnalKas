package com.sigarda.jurnalkas.data.network.model.profile.showprofile


import com.google.gson.annotations.SerializedName

data class ProfileShowResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?
)