package com.cwod.trasset.home.provider

import com.cwod.trasset.common.BaseModel
import com.cwod.trasset.helper.Urls
import com.cwod.trasset.home.provider.model.AssetListModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AssetListApi {

    @GET(Urls.ASSET_LIST)
    fun getAssetListResponse(): Single<BaseModel<List<AssetListModel>>>

    @GET(Urls.ASSET_LIST)
    fun getAssetListWithTypeResponse(@Query("type") type: String): Single<BaseModel<List<AssetListModel>>>

}