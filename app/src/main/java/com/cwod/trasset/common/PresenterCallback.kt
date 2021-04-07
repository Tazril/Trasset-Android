package com.cwod.trasset.common

interface PresenterCallback<T> {
    fun onSuccess(responseModel: T)
    fun onFailure(message: String)
}