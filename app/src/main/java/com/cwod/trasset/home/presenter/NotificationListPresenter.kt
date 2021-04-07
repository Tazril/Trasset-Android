package com.cwod.trasset.home.presenter

import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.home.provider.NotificationListProvider
import com.cwod.trasset.home.provider.model.NotificationModel
import com.cwod.trasset.home.view.HomeActivity

class NotificationListPresenter(var view: HomeActivity, var provider: NotificationListProvider) :
    BasePresenter() {

    fun getNotificationListResponse() {
        provider.getNotificationListResponse(object : PresenterCallback<List<NotificationModel>> {
            override fun onSuccess(responseModel: List<NotificationModel>) {
                view.onNotificationsRecieved(responseModel)
            }

            override fun onFailure(message: String) {
                view.onNotificationsRecieved(emptyList())
            }
        }).also { compositeDisposable.add(it) }
    }


}