package com.sigarda.jurnalkas.data.network.datasource

import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginResponse
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterResponse
import com.sigarda.jurnalkas.data.network.model.profile.showprofile.ProfileShowResponse
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.ProfileUpdateResponse
import com.sigarda.jurnalkas.data.network.service.AuthApiInterface
import javax.inject.Inject

interface AuthRemoteDataSource {
    suspend fun postLogin(loginRequestBody: LoginRequestBody): LoginResponse
    suspend fun postRegister(registerRequestBody: RegisterRequestBody): RegisterResponse

    suspend fun getProfile(token:String): ProfileShowResponse
    suspend fun updateProfile(token: String): ProfileUpdateResponse
}

class AuthRemoteDataSourceImpl @Inject constructor(private val apiService: AuthApiInterface) :
    AuthRemoteDataSource {

    override suspend fun getProfile(token:String): ProfileShowResponse{
        return apiService.getProfileUser(token)
    }
    override suspend fun updateProfile(token: String): ProfileUpdateResponse {
        return apiService.putProfile(token)
    }

    override suspend fun postRegister(registerRequestBody: RegisterRequestBody): RegisterResponse {
        return apiService.postRegister(registerRequestBody)
    }
    override suspend fun postLogin(loginRequestBody: LoginRequestBody): LoginResponse {
        return apiService.postLogin(loginRequestBody)
    }


}