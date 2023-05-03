package com.sigarda.jurnalkas.data.network.datasource

import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterResponse
import com.sigarda.jurnalkas.data.network.model.profile.showprofile.ProfileShowResponse
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.ProfileUpdateResponse
import com.sigarda.jurnalkas.data.network.model.transaction.getcategories.response.GetCategoriesResponse
import com.sigarda.jurnalkas.data.network.model.transaction.postcategories.requestbody.CategoriesRequestBody
import com.sigarda.jurnalkas.data.network.model.transaction.postcategories.response.PostCategoriesResponse
import com.sigarda.jurnalkas.data.network.model.transaction.wallet.requestbody.WalletRequestBody
import com.sigarda.jurnalkas.data.network.model.transaction.wallet.response.PostWalletResponse
import com.sigarda.jurnalkas.data.network.service.AuthApiInterface
import com.sigarda.jurnalkas.data.network.service.TransactionApiInterface
import com.sigarda.jurnalkas.data.repository.TransactionApiRepository
import javax.inject.Inject

interface TransactionRemoteDataSource {
    suspend fun postCategories(token : String, createCategries: CategoriesRequestBody): PostCategoriesResponse
    suspend fun getCategories(token:String): GetCategoriesResponse
    suspend fun inputWallet(token: String , createWallet: WalletRequestBody): PostWalletResponse
}

class TransactionRemoteDataSourceImpl @Inject constructor(private val apiService: TransactionApiInterface) : TransactionRemoteDataSource {

    override suspend fun postCategories(
        token: String,
        createCategries: CategoriesRequestBody
    ): PostCategoriesResponse {
        return apiService.postCategories(token, createCategries)
    }

    override suspend fun getCategories(token: String): GetCategoriesResponse {
        return apiService.getCategories(token)
    }

    override suspend fun inputWallet(
        token: String,
        createWallet: WalletRequestBody
    ): PostWalletResponse {
        return apiService.postWallet(token,createWallet)
    }


}
