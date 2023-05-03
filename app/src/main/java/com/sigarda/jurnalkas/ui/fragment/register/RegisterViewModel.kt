package com.sigarda.jurnalkas.ui.fragment.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sigarda.jurnalkas.data.local.preference.UserDataStoreManager
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterResponse
import com.sigarda.jurnalkas.data.repository.AuthApiRepository
import com.sigarda.jurnalkas.model.usecase.UseCases
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dataStoreManager: UserDataStoreManager,
    private val authRepository: AuthApiRepository,
    private val useCases: UseCases
) : ViewModel() {

    private val _authResult = MutableLiveData<SignUpState>()
    val authResult: LiveData<SignUpState> = _authResult

    private var _postRegisterUserResponse = MutableLiveData<Resource<RegisterResponse>>()
    val postRegisterUserResponse: LiveData<Resource<RegisterResponse>> get() = _postRegisterUserResponse


    fun postRegisterUser(registerRequestBody: RegisterRequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            val registerResponse = authRepository.postRegisterUser(registerRequestBody)
            viewModelScope.launch(Dispatchers.Main) {
                _postRegisterUserResponse.postValue(registerResponse)
            }
        }
    }
    fun signInWithEmailAndPassword(
        email: String?,
        name: String?,
        password: String?,
        verifyPassword: String?
    ) {
        if (
            !email.isNullOrBlank() &&
            !name.isNullOrBlank() &&
            !password.isNullOrBlank() &&
            !verifyPassword.isNullOrBlank() &&
            password == verifyPassword
        ) {

            viewModelScope.launch {
                _authResult.value = SignUpState.Loading(true)
                useCases.signUp(email,password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModelScope.launch {
                            useCases.getCurrentUserInfo()?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    _authResult.value = SignUpState
                                        .SendEmailIsSuccess("Please check your e-mail")
                                }?.addOnFailureListener {
                                    _authResult.value = SignUpState.SendEmailIsFailure("Please try again later")
                                }
                        }
                        _authResult.value = SignUpState.Loading(false)
                        _authResult.value = SignUpState.SignUpIsSuccess(task.result)
                    }
                }.addOnFailureListener {
                    _authResult.value = SignUpState.SignUpIsFailure(it.message.toString())
                }
            }
        }
    }

    fun saveUser(
        getFirebaseUserUid: String,
        email: String,
        name: String,
        password: String
    ) {
        viewModelScope.launch {
            useCases.saveUser(
                getFirebaseUserUid = getFirebaseUserUid,
                email = email,
                name = name,
                password = password
            )
        }

    }
}