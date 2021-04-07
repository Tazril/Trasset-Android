package com.cwod.trasset.common

import com.cwod.trasset.home.provider.model.NotificationModel

interface Notification {
    fun onNotificationsRecieved(notifications: List<NotificationModel>)
}