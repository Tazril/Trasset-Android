package com.cwod.trasset.authentication.model

import com.cwod.trasset.common.BaseModel
import com.cwod.trasset.helper.Urls
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST(Urls.SIGN_IN)
    fun getSignInResponse(@Body jsonObject: JsonObject): Single<BaseModel<AuthenticationModel>>

    @POST(Urls.SIGN_UP)
    fun getSignUpResponse(@Body jsonObject: JsonObject): Single<BaseModel<AuthenticationModel>>


}