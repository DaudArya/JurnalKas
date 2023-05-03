package com.sigarda.jurnalkas.data.repository

import com.sigarda.jurnalkas.data.local.datasource.UserLocalDataSource
import com.sigarda.jurnalkas.data.network.datasource.AuthRemoteDataSource
import com.sigarda.jurnalkas.data.network.datasource.TransactionRemoteDataSource
import com.sigarda.jurnalkas.data.network.model.transaction.getcategories.response.GetCategoriesResponse
import com.sigarda.jurnalkas.data.network.model.transaction.postcategories.requestbody.CategoriesRequestBody
import com.sigarda.jurnalkas.data.network.model.transaction.postcategories.response.PostCategoriesResponse
import com.sigarda.jurnalkas.data.network.model.transaction.wallet.requestbody.WalletRequestBody
import com.sigarda.jurnalkas.data.network.model.transaction.wallet.response.PostWalletResponse
import com.sigarda.jurnalkas.wrapper.Resource
import javax.inject.Inject

interface TransactionApiRepository {
    suspend fun postCategories(token: String, categoriesRequestBody: CategoriesRequestBody): Resource<PostCategoriesResponse>
    suspend fun getCategories(token : String): Resource<GetCategoriesResponse>
    suspend fun insertWallet(token: String , walletRequestBody: WalletRequestBody): Resource<PostWalletResponse>
}

class TransactionRepositoryImpl @Inject constructor( private val dataSource: TransactionRemoteDataSource)
    : TransactionApiRepository {

    override suspend fun postCategories(
        token: String,
        categoriesRequestBody: CategoriesRequestBody
    ): Resource<PostCategoriesResponse> {
        return proceed {
            dataSource.postCategories(token , categoriesRequestBody)
        }
    }


    override suspend fun getCategories(token: String): Resource<GetCategoriesResponse> {
        return proceed {
            dataSource.getCategories(token)
        }
    }

    override suspend fun insertWallet(
        token: String,
        walletRequestBody: WalletRequestBody
    ): Resource<PostWalletResponse> {
        return proceed {
            dataSource.inputWallet(token , walletRequestBody)
        }
    }


    private suspend fun <T> proceed(coroutines: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutines.invoke())
        } catch (e: Exception) {
            Resource.Error(e, e.message)
        }
    }


}

