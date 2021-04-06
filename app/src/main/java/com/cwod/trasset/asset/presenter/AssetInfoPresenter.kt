package com.cwod.trasset.asset.presenter

import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.asset.view.AssetInfoView
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.helper.PresenterCallback

class AssetInfoPresenter (var view : AssetInfoView, var provider : AssetTrackProvider) : BasePresenter()  {

     fun getAssetTrackResponse() {
        view.showProgressBar()
        provider.getAssetTrackResponse(object  : PresenterCallback<TrackWrapper> {
            override fun onSuccess(responseModel: TrackWrapper) {
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