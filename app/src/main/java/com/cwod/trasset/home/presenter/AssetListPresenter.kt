package com.cwod.trasset.home.presenter

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.cwod.trasset.R
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.home.provider.AssetListProvider
import com.cwod.trasset.home.provider.model.AssetListModel
import com.cwod.trasset.home.view.AssetListMapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AssetListPresenter(var view: AssetListMapView, var provider: AssetListProvider) :
    BasePresenter() {

    // null or empty type indicates all types
    fun getAssetListResponse(type: String?) {

        view.showProgressBar()
        if (type == null || type.isEmpty())
            provider.getAssetListResponse(object : PresenterCallback<List<AssetListModel>> {
                override fun onSuccess(responseModel: List<AssetListModel>) {
                    view.loadResponse(responseModel)
                    setMarkerOptions(responseModel)
                    setAutoSearchConfig(responseModel)
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
                        setMarkerOptions(responseModel)
                        setAutoSearchConfig(responseModel)
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

    fun setMarkerOptions(responseModel: List<AssetListModel>) {
        Single.just(responseModel)
            .map { list ->

                list.map { asset ->
                    val latLng = LatLng(asset.lat, asset.lon)
                    val pickupMarkerDrawable = ContextCompat.getDrawable(
                        view.requireContext(),
                        if (asset.type == "truck") R.drawable.truck else R.drawable.man
                    )
                    val factor = if (asset.type == "truck") 8 else 50
                    val icon = BitmapDescriptorFactory.fromBitmap(
                        pickupMarkerDrawable?.toBitmap(
                            pickupMarkerDrawable.intrinsicWidth / factor,
                            pickupMarkerDrawable.intrinsicHeight / factor,
                            null
                        )
                    )

                    Pair(
                        MarkerOptions().position(latLng).title(asset.name).snippet(asset.desc)
                            .icon(icon), asset
                    )
                }
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    it?.let {
                        view.setMarkers(it)
                    }
                },
                onError = { }
            ).also { compositeDisposable.add(it) }
    }

    fun setAutoSearchConfig(responseModel: List<AssetListModel>) {
        Single.just(responseModel)
            .map { list ->
                Pair(
                    list.map { asset ->
                        LatLng(asset.lat, asset.lon)
                    },
                    list.map { it.name }
                )
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    it?.let {
                        view.setUpAutoCompleteSearch(it.first, it.second)
                    }
                },
                onError = { }
            ).also { compositeDisposable.add(it) }
    }

}