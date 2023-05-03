package com.sigarda.jurnalkas.wrapper

sealed class Resource<T>
    ( val message: String? = null ,
      val payload: T? = null,
      val exception: Exception? = null
) {
    class Success<T>(val data: T?) : Resource<T>(data?.toString())
    class Error<T>(exception: Exception?, message: String?) :
        Resource<T>(message = message, exception = exception)
    class Loading<T>(data: T? = null) : Resource<T>(data.toString())
}

