package com.cwod.trasset.authentication.model

import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.helper.ApiClient
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AuthenticationProvider(val jsonObject: JsonObject) {


    fun getUserSignInResponse(callback: PresenterCallback<AuthenticationModel>): Disposable {

        return ApiClient.retroClient.create(AuthenticationApi::class.java)
            .getSignInResponse(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it.error?.message == null) it.data?.apply { callback.onSuccess(this) }
                    else callback.onFailure("Error: ${it.error.message}")
                },
                onError = {
                    callback.onFailure(
                        if (it.message != null && it.message!!.contains("401"))
                            "Invalid Credentials" else "Unable to Login"
                    )
                }
            )
    }

    fun getUserSignUpResponse(callback: PresenterCallback<AuthenticationModel>): Disposable {
        return ApiClient.retroClient.create(AuthenticationApi::class.java)
            .getSignUpResponse(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> Timber.log(2, t.message.toString()) }
            .subscribeBy(
                onSuccess = {
                    if (it.error?.message == null) it.data?.apply { callback.onSuccess(this) }
                    else callback.onFailure("Error: ${it.error.message}")
                },
                onError = {
                    callback.onFailure(
                        if (it.message != null && it.message!!.contains("422"))
                            "Email Already in Use" else "Unable to Register"
                    )
                }
            )
    }


}