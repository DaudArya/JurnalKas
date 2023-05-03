package com.sigarda.jurnalkas.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sigarda.jurnalkas.data.local.preference.UserDataStoreManager
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginResponse
import com.sigarda.jurnalkas.data.repository.AuthApiRepository
import com.sigarda.jurnalkas.model.usecase.UseCases
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: UserDataStoreManager,
    private val authRepository: AuthApiRepository,
    private val useCases: UseCases
) : ViewModel() {

    private var _postLoginUserResponse = MutableLiveData<Resource<LoginResponse>>()
    val postLoginUserResponse: LiveData<Resource<LoginResponse>> get() = _postLoginUserResponse

    private val _authResult = MutableLiveData<SignInState>()
    val authResult: LiveData<SignInState> = _authResult


    fun signInWithEmailAndPassword(email: String?, password: String?) {
        if (
            !email.isNullOrBlank() &&
            !password.isNullOrBlank()
        ) {
            viewModelScope.launch {
                _authResult.value = SignInState.Loading(true)
                useCases.signIn(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        viewModelScope.launch{
                            val verification =  useCases.getCurrentUserInfo()?.isEmailVerified
                            verification?.let {
                                if (it) {
                                    _authResult.value = SignInState.Loading(false)
                                    _authResult.value = SignInState.VerificationIsSuccess(task.result)
                                }else {
                                    _authResult.value = SignInState.VerificationIsFailure("Please check your e-mail")
                                }
                            }
                        }
                    }
                }.addOnFailureListener {
                    _authResult.value = SignInState.SignInIsFailure(it.message.toString())
                }
            }

        }
    }


    fun statusLogin(isLogin: Boolean) {
        viewModelScope.launch {
            dataStoreManager.statusLogin(isLogin)
        }
    }

    fun getLoginStatus(): LiveData<Boolean> {
        return dataStoreManager.getLoginStatus().asLiveData()
    }

    fun SaveUserToken(isToken: String) {
        viewModelScope.launch {
            authRepository.SaveUserToken(isToken)
        }
    }

    fun getDataStoreToken(): LiveData<String> {
        return dataStoreManager.getToken.asLiveData()
    }

    fun login(loginRequestBody: LoginRequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            val loginResponse = authRepository.postLoginUser(loginRequestBody)
            viewModelScope.launch(Dispatchers.Main) {
                _postLoginUserResponse.postValue(loginResponse)
            }
        }
    }

    fun setUserLogin(isLogin: Boolean) {
        viewModelScope.launch {
            authRepository.setUserLogin(isLogin)
        }
    }

    fun setUserToken(isToken: String) {
        viewModelScope.launch {
            authRepository.setUserToken(isToken)
        }
    }

    fun getUserLoginStatus(): LiveData<Boolean> {
        return authRepository.getUserLoginStatus().asLiveData()
    }

}