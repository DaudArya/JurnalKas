package com.sigarda.jurnalkas.wrapper.viewState

import com.sigarda.jurnalkas.data.local.entity.TransactionEntity

sealed class ViewState {
    object Loading : ViewState()
    object Empty : ViewState()
    data class Success(val transaction: List<TransactionEntity>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
}
