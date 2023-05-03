package com.sigarda.jurnalkas.data.repository

import com.sigarda.jurnalkas.data.local.datasource.UserLocalDataSource
import com.sigarda.jurnalkas.data.network.datasource.AuthRemoteDataSource
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginResponse
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterResponse
import com.sigarda.jurnalkas.data.network.model.profile.showprofile.ProfileShowResponse
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.ProfileUpdateResponse
import com.sigarda.jurnalkas.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AuthApiRepository {
    suspend fun postRegisterUser(registerRequestBody: RegisterRequestBody): Resource<RegisterResponse>
    suspend fun postLoginUser(loginRequestBody: LoginRequestBody): Resource<LoginResponse>
    suspend fun GetProfileData(token : String): Resource<ProfileShowResponse>
    suspend fun PutProfileData(token: String): Resource<ProfileUpdateResponse>
    suspend fun setUserLogin(isLogin: Boolean)
    suspend fun setUserToken(isToken: String)
    suspend fun SaveUserToken(isToken: String)
    fun getUserLoginStatus(): Flow<Boolean>
}

class UserRepositoryImpl @Inject constructor(private val userLocalDataSource: UserLocalDataSource, private val dataSource: AuthRemoteDataSource) : AuthApiRepository {

    override suspend fun postRegisterUser(registerRequestBody: RegisterRequestBody): Resource<RegisterResponse> {
        return proceed {
            dataSource.postRegister(registerRequestBody)
        }
    }

    override suspend fun postLoginUser(loginRequestBody: LoginRequestBody): Resource<LoginResponse> {
        return proceed {
            dataSource.postLogin(loginRequestBody)
        }
    }

    override suspend fun GetProfileData(token: String): Resource<ProfileShowResponse> {
        return proceed {
            dataSource.getProfile(token)
        }
    }

    override suspend fun PutProfileData(token: String): Resource<ProfileUpdateResponse> {
        return proceed {
            dataSource.updateProfile(token)
        }
    }

    override suspend fun setUserLogin(isLogin: Boolean) {
        return userLocalDataSource.setUserLogin(isLogin)
    }

    override suspend fun setUserToken(isToken: String) {
        return userLocalDataSource.setUserToken(isToken)
    }

    override suspend fun SaveUserToken(isToken: String) {
        return userLocalDataSource.SaveUserToken(isToken)
    }

    override fun getUserLoginStatus(): Flow<Boolean> {
        return userLocalDataSource.getUserLoginStatus()
    }


    private suspend fun <T> proceed(coroutines: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutines.invoke())
        } catch (e: Exception) {
            Resource.Error(e, e.message)
        }
    }

}