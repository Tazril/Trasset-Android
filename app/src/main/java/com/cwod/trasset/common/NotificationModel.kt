package com.cwod.trasset.common

data class NotificationModel(
    val lat: Double,
    val lon: Double,
    val type: String,
    val status: String,
    val timestamp: String,
    val name: String,
    val assetId: String,
    val _id: String
)