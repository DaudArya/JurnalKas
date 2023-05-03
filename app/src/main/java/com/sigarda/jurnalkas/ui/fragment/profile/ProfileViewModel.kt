package com.sigarda.jurnalkas.ui.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sigarda.jurnalkas.data.local.datastore.UserDataStore
import com.sigarda.jurnalkas.data.local.preference.UserDataStoreManager
import com.sigarda.jurnalkas.data.network.model.profile.showprofile.ProfileShowResponse
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.Data
import com.sigarda.jurnalkas.data.network.model.profile.updateprofile.ProfileUpdateResponse
import com.sigarda.jurnalkas.data.network.service.AuthApiInterface
import com.sigarda.jurnalkas.data.repository.AuthApiRepository
import com.sigarda.jurnalkas.model.usecase.UseCases
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val dataStoreManager: UserDataStoreManager,
    private val ApiClient: AuthApiInterface,
    private val preferences: UserDataStore,
    private val userRepository: AuthApiRepository,
    private val useCases: UseCases
) : ViewModel() {


    private var _ProfileResponse = MutableLiveData<Resource<ProfileShowResponse>>()
    val GetProfileUserResponse: LiveData<Resource<ProfileShowResponse>> get() = _ProfileResponse
    private var _ProfilePutResponse = MutableLiveData<Resource<ProfileUpdateResponse>>()


    private val _data: MutableLiveData<ProfileShowResponse> = MutableLiveData()
    val data: LiveData<ProfileShowResponse?> get() = _data
    private val _update: MutableLiveData<Data?> = MutableLiveData()
    val update: LiveData<Data?> get() = _update

    private val _userData = MutableLiveData(ProfileState())
    val userData: LiveData<ProfileState> = _userData
    private val _updateResult = MutableLiveData(ProfileState())
    val updateResult: LiveData<ProfileState> = _updateResult
    val email = preferences.getEmail().asLiveData()

    fun signOut() {
        viewModelScope.launch {
            useCases.signOut()
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

    fun GetProfileUser(token : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ProfileGet = userRepository.GetProfileData(token)
            viewModelScope.launch(Dispatchers.Main) {
                _ProfileResponse.postValue(ProfileGet)
            }
        }
    }

    fun updateUser(
        name : RequestBody,
        userName : RequestBody,
        image: MultipartBody.Part,
        token: String
    ){
        ApiClient.updateUser(name,userName,image,token)
            .enqueue(object : Callback<Data> {
                override fun onResponse(
                    call: Call<Data>,
                    response: Response<Data>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _update.postValue(responseBody)
                        }
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    _update.postValue(null)
                }
            })
    }




    fun logout() {
        preferences.logout()
    }
}