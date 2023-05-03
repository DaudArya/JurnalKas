package com.sigarda.jurnalkas.data.network.service

import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginResponse
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterResponse
import com.sigarda.jurnalkas.data.network.model.profile.showprofile.ProfileShowResponse
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.Data
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.ProfileUpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AuthApiInterface {
    @POST("register")
    suspend fun postRegister(
        @Body registerRequestBody: RegisterRequestBody
    ): RegisterResponse

    @POST("login")
    suspend fun postLogin(
        @Body loginRequestBody: LoginRequestBody
    ): LoginResponse

    @POST("account")
    suspend fun getProfileUser(
        @Header("Authorization") token: String
    ): ProfileShowResponse

    @Multipart
    @POST("account/update")
    fun updateUser(
        @Part("name") lastName: RequestBody,
        @Part("username") username: RequestBody,
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String): Call<Data>


    @POST("account/update")
    suspend fun putProfile(token: String): ProfileUpdateResponse
}