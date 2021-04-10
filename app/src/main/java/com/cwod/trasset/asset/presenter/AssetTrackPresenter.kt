package com.cwod.trasset.asset.presenter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.content.ContextCompat
import com.cwod.trasset.R
import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.GeoFenceModel
import com.cwod.trasset.asset.provider.model.GeoRouteModel
import com.cwod.trasset.asset.provider.model.TrackItemModel
import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.asset.view.AssetTrackView
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.helper.DataFormatter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime

class AssetTrackPresenter(var view: AssetTrackView, var provider: AssetTrackProvider) :
    BasePresenter() {

    override fun onCleared() {
        view.hideProgressBar()
        super.onCleared()
    }

    fun getAssetTrackResponse() {
        view.showProgressBar()
        provider.getAssetTrackResponse(object : PresenterCallback<TrackWrapper> {
            override fun onSuccess(responseModel: TrackWrapper) {
                view.loadResponse(responseModel)
                responseModel.geoFence?.apply { setPolygonOptions(this) }
                responseModel.geoRoute?.apply { setPolylineOptions(this) }
                view.hideProgressBar()
            }

            override fun onFailure(message: String) {
                view.show(message)
                view.hideProgressBar()
            }
        }).also { compositeDisposable.add(it) }
    }

    fun getAssetTrackByTimeResponse(start: String, end: String) {
        view.showProgressBar()
        provider.getAssetTrackByTimeResponse(start, end, object : PresenterCallback<TrackWrapper> {
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

    fun setPolygonOptions(geoFence: GeoFenceModel) {
        Single.just(geoFence)
            .map { geoFenceObj ->
                var polygonOptions = PolygonOptions()
                    .strokeColor(Color.RED)
                    .strokeWidth(2.0f)
                    .fillColor(
                        ContextCompat.getColor(
                            view.requireContext(),
                            R.color.fenceColorAlpha
                        )
                    )
                geoFenceObj.geometry.coordinates?.get(0)?.forEach { arr ->
                    polygonOptions = polygonOptions.add(LatLng(arr[1], arr[0]))
                }
                polygonOptions
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    it?.let {
                        view.setPolygon(it)
                    }
                },
                onError = { }
            ).also { compositeDisposable.add(it) }
    }


    fun setPolylineOptions(geoRouteModel: GeoRouteModel) {
        Single.just(geoRouteModel)
            .map { geoRoute ->
                var polylineOptions = PolylineOptions()
                    .color(Color.BLUE)
                    .width(5.0f)
                geoRoute.geometry.coordinates?.forEach { arr ->
                    polylineOptions = polylineOptions.add(LatLng(arr[1], arr[0]))
                }
                polylineOptions
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    it?.let {
                        view.setPolyline(it)
                    }
                },
                onError = { }
            ).also { compositeDisposable.add(it) }
    }

    fun getGoogleMapsIntent(data: TrackItemModel): Intent {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${data.lat},${data.lon}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        return mapIntent
    }

    fun getPopupMessage(data: TrackItemModel) = "Asset Located [Lat: ${
        DataFormatter.getInstance().formatDecimalPlaces(data.lat)
    },Lon: ${
        DataFormatter.getInstance().formatDecimalPlaces(data.lon)
    }] at ${DataFormatter.getInstance().getMediumDateTime(DateTime(data.timestamp).millis)}"
}