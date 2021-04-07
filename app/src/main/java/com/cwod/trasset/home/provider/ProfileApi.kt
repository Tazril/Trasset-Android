package com.cwod.trasset.home.provider

import com.cwod.trasset.common.BaseModel
import com.cwod.trasset.helper.Urls
import com.cwod.trasset.home.provider.model.ProfileModel
import io.reactivex.Single
import retrofit2.http.GET

interface ProfileApi {

    @GET(Urls.PROFILE)
    fun getProfileResponse(): Single<BaseModel<ProfileModel>>

}