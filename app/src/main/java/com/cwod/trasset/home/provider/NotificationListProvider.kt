package com.cwod.trasset.home.provider

import com.cwod.trasset.common.NotificationModel
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.helper.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class NotificationListProvider {

    fun getNotificationListResponse(callback: PresenterCallback<List<NotificationModel>>): Disposable {
        return ApiClient.retroClientCache.create(NotificationListApi::class.java)
            .getNotificationListResponse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it.error?.message == null)
                        it.data?.apply { callback.onSuccess(this) }
                    else callback.onFailure("Error: ${it.error.message}")
                },
                onError = { callback.onFailure(it.message ?: "Some Error Occurred") }
            )

    }

}