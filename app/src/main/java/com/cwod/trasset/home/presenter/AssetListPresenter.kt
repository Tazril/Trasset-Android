package com.cwod.trasset.home.presenter

import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.helper.PresenterCallback
import com.cwod.trasset.home.provider.AssetListProvider
import com.cwod.trasset.home.provider.model.AssetListModel
import com.cwod.trasset.home.provider.model.SessionListModel
import com.cwod.trasset.home.view.AssetsMapView

class AssetListPresenter (var view : AssetsMapView, var provider : AssetListProvider) : BasePresenter()  {

     fun getAssetListResponse() {
        view.showProgressBar()
        provider.getAssetListResponse(object  : PresenterCallback<List<AssetListModel>> {
            override fun onSuccess(responseModel: List<AssetListModel>) {
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