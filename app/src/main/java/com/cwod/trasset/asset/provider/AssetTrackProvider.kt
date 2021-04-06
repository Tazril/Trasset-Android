package com.cwod.trasset.asset.provider

import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.helper.ApiClient
import com.cwod.trasset.helper.GeneralModel
import com.cwod.trasset.helper.PresenterCallback
import com.cwod.trasset.home.provider.model.AssetListModel
import com.cwod.trasset.home.provider.model.SessionCreateModel
import com.cwod.trasset.home.provider.model.SessionListModel
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AssetTrackProvider(val assetId:String) {

    fun getAssetTrackResponse(callback: PresenterCallback<TrackWrapper>): Disposable {
        return ApiClient.retroClientCache.create(AssetTrackApi::class.java).getAssetTrackResponse(assetId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if(it.error?.message==null)
                        it.data?.apply { callback.onSuccess(this)}
                    else    callback.onFailure("Error: ${it.error.message}")
                },
                onError = { callback.onFailure(it.message?:"Some Error Occurred") }
            )

    }
    fun getAssetTrackByTimeResponse(start:String,end:String,callback: PresenterCallback<TrackWrapper>): Disposable {
        return ApiClient.retroClientCache.create(AssetTrackApi::class.java).getAssetTrackByTimeResponse(assetId,start,end)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if(it.error?.message==null)
                        it.data?.apply { callback.onSuccess(this)}
                    else    callback.onFailure("Error: ${it.error.message}")
                },
                onError = { callback.onFailure(it.message?:"Some Error Occurred") }
            )

    }
}