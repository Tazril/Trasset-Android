package com.cwod.trasset.helper

interface PresenterCallback<T> {
    fun onSuccess(responseModel: T)
    fun onFailure(message: String)
}