package com.cwod.trasset.home.provider

import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.helper.ApiClient
import com.cwod.trasset.home.provider.model.AssetListModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AssetListProvider {

    fun getAssetListResponse(callback: PresenterCallback<List<AssetListModel>>): Disposable {
        return ApiClient.retroClientCache.create(AssetListApi::class.java).getAssetListResponse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it.error?.message == null)
                        it.data?.apply { callback.onSuccess(this) }
                    else callback.onFailure("Error: ${it.error.message}")
                },
                onError = { callback.onFailure(it.message ?: "Some Error Occurred") }
            )

    }

    fun getAssetListWithTypeResponse(
        type: String,
        callback: PresenterCallback<List<AssetListModel>>
    ): Disposable {
        return ApiClient.retroClientCache.create(AssetListApi::class.java)
            .getAssetListWithTypeResponse(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it.error?.message == null)
                        it.data?.apply { callback.onSuccess(this) }
                    else callback.onFailure("Error: ${it.error.message}")
                },
                onError = { callback.onFailure(it.message ?: "Some Error Occurred") }
            )

    }
}