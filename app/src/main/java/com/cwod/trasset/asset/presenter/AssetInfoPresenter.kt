package com.cwod.trasset.asset.presenter

import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.AssetModel
import com.cwod.trasset.asset.view.AssetInfoView
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback

class AssetInfoPresenter(var view: AssetInfoView, var provider: AssetTrackProvider) :
    BasePresenter() {

    fun getAssetTrackResponse() {
        view.showProgressBar()
        provider.getAssetInfoResponse(object : PresenterCallback<AssetModel> {
            override fun onSuccess(responseModel: AssetModel) {
                view.loadResponse(responseModel)
                view.hideProgressBar()
            }

            override fun onFailure(message: String) {
                view.show(message)
                view.hideProgressBar()
            }
        }).also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        view.hideProgressBar()
        super.onCleared()
    }


}