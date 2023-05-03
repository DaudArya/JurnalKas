package com.sigarda.jurnalkas.ui.fragment.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sigarda.jurnalkas.data.local.preference.UserDataStoreManager
import com.sigarda.jurnalkas.model.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val useCases: UseCases,
    private val dataStoreManager: UserDataStoreManager,
): ViewModel() {

    val email = dataStoreManager.getEmail().asLiveData()

    fun getLoginStatus(): LiveData<Boolean> {
        return dataStoreManager.getLoginStatus().asLiveData()
    }

    private val _isCurrentUserExist = MutableLiveData<FirebaseUser?>()
    val isCurrentUserExist: LiveData<FirebaseUser?> = _isCurrentUserExist

    init {
        getCurrentUserInfo()
    }
    fun getCurrentUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(2000)
            _isCurrentUserExist.value = useCases.getCurrentUserInfo()
        }
    }


}