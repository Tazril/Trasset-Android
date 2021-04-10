package com.cwod.trasset.asset.provider

import com.cwod.trasset.common.BaseModel
import com.cwod.trasset.common.NotificationModel
import com.cwod.trasset.helper.Urls
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationListApi {

    @GET(Urls.ASSET_NOTIFICATION_LIST)
    fun getNotificationListResponse(@Path("id") id: String): Single<BaseModel<List<NotificationModel>>>

}