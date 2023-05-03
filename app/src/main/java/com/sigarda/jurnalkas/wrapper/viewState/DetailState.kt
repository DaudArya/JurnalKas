package com.sigarda.jurnalkas.wrapper.viewState

import com.sigarda.jurnalkas.data.local.entity.TransactionEntity

sealed class DetailState {
    object Loading : DetailState()
    object Empty : DetailState()
    data class Success(val transaction: TransactionEntity) : DetailState()
    data class Error(val exception: Throwable) : DetailState()
}
