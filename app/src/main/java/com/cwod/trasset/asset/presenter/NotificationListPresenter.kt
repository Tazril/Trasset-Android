package com.cwod.trasset.asset.presenter

import com.cwod.trasset.asset.provider.NotificationListProvider
import com.cwod.trasset.asset.view.AssetActivity
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.home.provider.model.NotificationModel

class NotificationListPresenter(var view: AssetActivity, var provider: NotificationListProvider) :
    BasePresenter() {

    fun getNotificationListResponse(id: String) {
        provider.getNotificationListResponse(id,
            object : PresenterCallback<List<NotificationModel>> {
                override fun onSuccess(responseModel: List<NotificationModel>) {
                    view.onNotificationsRecieved(responseModel)
                }

                override fun onFailure(message: String) {
                    view.onNotificationsRecieved(emptyList())
                }
            }).also { compositeDisposable.add(it) }
    }


}