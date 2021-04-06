package com.cwod.trasset.helper

object Urls {

    var TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InBlcnVsMzY1QGdtYWlsLmNvbSIsImlhdCI6MTYxNzA5NjE2MX0.5TuckzbPnBUjo6x_DpWBCIdT3TswvKptQE_YEk87i88"

    const val BASE_URL = "https://jumbotail.herokuapp.com"// "http://127.0.0.1:8000"
    const val SIGN_IN = "/auth/signin/"
    const val SIGN_UP = "/auth/signup/"
    const val SIGN_UP_OTP = "verify_otp/"
    const val RESET_PASSWORD = "reset_password/"
    const val FORGOT_PASSWORD = "forgot_password/"
    const val SESSION_CREATE = "session/create/"
    const val SESSION_JOIN =  "session/join/"
    const val SESSION_LIST = "session/list/"
    const val SESSION_MEMBERS = "session/member/list/"
    const val TRANSACTION_GROUP_CREATE = "'transaction/group/create/"
    const val TRANSACTION_GROUP_LIST = "transaction/group/list/"
    const val ALL_TRANSACTION_LIST = "transaction/list/session"

    const val ASSET_LIST = "/api/asset/list/"
    const val PROFILE = "/user/getuser"
    const val ASSET_TRACK = "/api/asset/track/{id}"
    const val ASSET_TRACK_TIME = "/api/asset/trackbytime/{id}"
}