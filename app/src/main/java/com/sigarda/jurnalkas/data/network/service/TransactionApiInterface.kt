package com.sigarda.jurnalkas.data.network.service

import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterResponse
import com.sigarda.jurnalkas.data.network.model.profile.showprofile.ProfileShowResponse
import com.sigarda.jurnalkas.data.network.model.transaction.getcategories.response.GetCategoriesResponse
import com.sigarda.jurnalkas.data.network.model.transaction.postcategories.requestbody.CategoriesRequestBody
import com.sigarda.jurnalkas.data.network.model.transaction.postcategories.response.PostCategoriesResponse
import com.sigarda.jurnalkas.data.network.model.transaction.wallet.requestbody.WalletRequestBody
import com.sigarda.jurnalkas.data.network.model.transaction.wallet.response.PostWalletResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TransactionApiInterface {

    @POST("categories")
    suspend fun postCategories(
        @Header("Authorization") token: String,
        @Body categoryRequestBody: CategoriesRequestBody
    ): PostCategoriesResponse

    @POST("wallet")
    suspend fun postWallet(
        @Header("Authorization") token: String,
        @Body walletRequestBody: WalletRequestBody
    ): PostWalletResponse


    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): GetCategoriesResponse


}