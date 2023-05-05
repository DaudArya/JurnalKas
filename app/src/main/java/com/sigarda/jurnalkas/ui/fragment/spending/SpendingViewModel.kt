package com.sigarda.jurnalkas.ui.fragment.spending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sigarda.jurnalkas.data.repository.TransactionRepository
import com.sigarda.jurnalkas.model.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class SpendingViewModel @Inject constructor(
    private val useCases: UseCases,
    private val repository: TransactionRepository
) : ViewModel() {

    private val _deleteBudget = MutableLiveData<BudgetState>()
    val deleteBudget: LiveData<BudgetState> = _deleteBudget

    private val _updateBudget = MutableLiveData<BudgetState>()
    val updateBudget: LiveData<BudgetState> = _updateBudget

    private val _addBudget = MutableLiveData<BudgetState>()
    val addBudget: LiveData<BudgetState> = _addBudget


    fun deleteTransactionById (id:Int){
        repository.deleteById(id)
    }

    fun addBudget(amount: Float?,title: String?, isIncome: Boolean?, type: String?, date: Date?) {
        viewModelScope.launch {
            val currentUser = useCases.getCurrentUserInfo()
            if (
                amount != null &&
                title != null &&
                isIncome != null &&
                type != null &&
                date != null &&
                currentUser != null
            ) {
                useCases.saveBudget(
                    amount = amount,
                    title = title,
                    isIncome = isIncome,
                    type = type,
                    date = date,
                    currentUserId = currentUser.uid
                ).addOnSuccessListener {
                    _addBudget.value = BudgetState.OnSuccess("Data Added")
                }.addOnFailureListener {
                    _addBudget.value = BudgetState.OnFailure(it.message.toString())
                }
            }else {
                _addBudget.value = BudgetState.OnFailure("Please check your data")
            }

        }
    }

    fun deleteBudget(documentId: String) {
        viewModelScope.launch {
            useCases.deleteBudget(documentId = documentId).addOnSuccessListener {
                _deleteBudget.value = BudgetState.OnSuccess("Data Deleted!")
            }.addOnFailureListener {
                _deleteBudget.value = BudgetState.OnFailure(it.message.toString())
            }
        }
    }

    fun updateBudget(
        amount: Float?,
        title: String?,
        isIncome: Boolean?,
        type: String?,
        date: Date?,
        documentId: String?
    ) {
        viewModelScope.launch {
            val currentUser = useCases.getCurrentUserInfo()
            if (
                amount != null &&
                title != null &&
                isIncome != null &&
                type != null &&
                date != null &&
                documentId != null &&
                currentUser != null
            ) {
                useCases.updateBudget(
                    amount = amount,
                    title = title,
                    isIncome = isIncome,
                    type = type,
                    date = date,
                    currentUserId = currentUser.uid,
                    documentId = documentId
                ).addOnSuccessListener {
                    _updateBudget.value = BudgetState.OnSuccess("Data Updated")
                }.addOnFailureListener {
                    _updateBudget.value = BudgetState.OnFailure(it.message.toString())
                }
            } else {
                _updateBudget.value = BudgetState.OnFailure("Please check your data!!")
            }
        }
    }
}
