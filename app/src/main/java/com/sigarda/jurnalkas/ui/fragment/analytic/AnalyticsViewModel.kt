package com.sigarda.jurnalkas.ui.fragment.analytic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.model.usecase.UseCases
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _result = MutableLiveData<Resource<List<TransactionUiModel>>>()
    val result: LiveData<Resource<List<TransactionUiModel>>> = _result

    fun getBudgetFromFirestore() {
        viewModelScope.launch {
            _result.value = useCases.getBudgetFromFirestore()
        }
    }

}