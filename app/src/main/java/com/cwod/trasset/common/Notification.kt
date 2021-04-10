package com.cwod.trasset.common

interface Notification {
    fun onNotificationsRecieved(notifications: List<NotificationModel>)
}