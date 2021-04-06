package com.cwod.trasset.home.provider

import com.cwod.trasset.helper.BaseModel
import com.cwod.trasset.helper.GeneralModel
import com.cwod.trasset.helper.Urls
import com.cwod.trasset.home.provider.model.AssetListModel
import com.cwod.trasset.home.provider.model.ProfileModel
import com.cwod.trasset.home.provider.model.SessionCreateModel
import com.cwod.trasset.home.provider.model.SessionListModel
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileApi {

    @GET(Urls.PROFILE)
    fun getProfileResponse() : Single<BaseModel<ProfileModel>>

}