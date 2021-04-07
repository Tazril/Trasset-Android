package com.cwod.trasset.home.provider.model

data class NotificationModel(
    val lat: Double,
    val lon: Double,
    val type: String,
    val status: String,
    val timestamp: String,
    val seenBy: List<String>?,
    val name: String,
    val assetId: String,
    val _id: String
)