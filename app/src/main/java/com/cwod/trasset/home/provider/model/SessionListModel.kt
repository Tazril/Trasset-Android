package com.cwod.trasset.home.provider.model

data class SessionListModel(
    var message: String,
    var success: Boolean,
    var data: List<SessionDataModel>
)