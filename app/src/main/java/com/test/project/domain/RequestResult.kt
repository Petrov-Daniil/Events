package com.test.project.domain

import com.test.project.data.remote.network.NetworkErrors

sealed class RequestResult<out R> {

    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error<out T>(val exception: NetworkErrors) : RequestResult<T>()
    //object Loading : RequestResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            // Loading -> "Loading"
        }
    }
}

val RequestResult<*>.succeeded
    get() = this is RequestResult.Success && data != null

