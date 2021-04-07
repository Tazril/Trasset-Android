package com.cwod.trasset.home.presenter

import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.home.provider.AssetListProvider
import com.cwod.trasset.home.provider.model.AssetListModel
import com.cwod.trasset.home.view.AssetListMapView

class AssetListPresenter(var view: AssetListMapView, var provider: AssetListProvider) :
    BasePresenter() {

    // null or empty type indicates all types
    fun getAssetListResponse(type: String?) {

        view.showProgressBar()
        if (type == null || type.isEmpty())
            provider.getAssetListResponse(object : PresenterCallback<List<AssetListModel>> {
                override fun onSuccess(responseModel: List<AssetListModel>) {
                    view.loadResponse(responseModel)
                    view.hideProgressBar()
                }

                override fun onFailure(message: String) {
                    view.show(message)
                    view.hideProgressBar()
                }
            }).also { compositeDisposable.add(it) }
        else
            provider.getAssetListWithTypeResponse(type,
                object : PresenterCallback<List<AssetListModel>> {
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