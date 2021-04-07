package com.cwod.trasset.home.provider

import com.cwod.trasset.common.BaseModel
import com.cwod.trasset.helper.Urls
import com.cwod.trasset.home.provider.model.NotificationModel
import io.reactivex.Single
import retrofit2.http.GET

interface NotificationListApi {

    @GET(Urls.NOTIFICATION_LIST)
    fun getNotificationListResponse(): Single<BaseModel<List<NotificationModel>>>

}