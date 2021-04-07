package com.cwod.trasset.asset.provider

import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.common.BaseModel
import com.cwod.trasset.helper.Urls
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetTrackApi {

    @GET(Urls.ASSET_TRACK)
    fun getAssetTrackResponse(@Path("id") assetId: String): Single<BaseModel<TrackWrapper>>

    @GET(Urls.ASSET_TRACK_TIME)
    fun getAssetTrackByTimeResponse(
        @Path("id") assetId: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Single<BaseModel<TrackWrapper>>

}