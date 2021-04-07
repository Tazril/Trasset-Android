package com.cwod.trasset.home.provider.model


data class SessionDataModel(
    var sessionId: Long,
    var sessionName: String,
    var sessionToken: String?,
    var createdOn: String,
    var createdBy: String,
    var createdById: Int
)