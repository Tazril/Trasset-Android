package com.cwod.trasset.asset.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.cwod.trasset.R
import com.cwod.trasset.asset.presenter.AssetTrackPresenter
import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_asset.*


class AssetTrackView : BaseFragment<TrackWrapper>(), OnMapReadyCallback,
    DateRangeSelector {
    companion object {
        const val TAG = "AssetsMapView"

    }

    lateinit var presenter: AssetTrackPresenter
    val SYDNEY = LatLng(-33.862, 151.21)
    val ZOOM_LEVEL = 3f


    override val layoutId: Int = R.layout.fragment_track

    var _googleMap
        get() = (requireActivity() as AssetActivity)._googleMap
        set(it) {
            (requireActivity() as AssetActivity)._googleMap = it
        }
    var _polygon
        get() = (requireActivity() as AssetActivity)._polygon
        set(it) {
            (requireActivity() as AssetActivity)._polygon = it
        }
    var _polyline
        get() = (requireActivity() as AssetActivity)._polyline
        set(it) {
            (requireActivity() as AssetActivity)._polyline = it
        }

    override fun initView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val id = requireActivity().intent.getStringExtra("id") ?: "none"
        presenter = AssetTrackPresenter(this, AssetTrackProvider(id))
        presenter.getAssetTrackResponse()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            setInfoWindowAdapter(Popup(requireContext(), markersToData))
            _googleMap = this
            requireActivity()
        }
    }

    override fun loadResponse(responseModel: TrackWrapper) {
        if (_googleMap == null) return
        if (responseModel.track.isNotEmpty())
            _googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        responseModel.track[0].lat,
                        responseModel.track[0].lon
                    ), ZOOM_LEVEL
                )
            )
        requireActivity().assetName.text = responseModel.asset_data.name

        responseModel.geoFence?.apply {
            var polygonOptions = PolygonOptions()
                .strokeColor(Color.RED)
                .strokeWidth(2.0f)
                .fillColor(ContextCompat.getColor(requireContext(), R.color.fenceColorAlpha))
            geometry.coordinates?.get(0)?.forEach { arr ->
                polygonOptions = polygonOptions.add(LatLng(arr[1], arr[0]))
            }
            _polygon = _googleMap?.addPolygon(polygonOptions)
            _polygon?.isVisible = false
        }

        responseModel.geoRoute?.apply {
            var polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(5.0f)
            geometry.coordinates?.forEach { arr ->
                polylineOptions = polylineOptions.add(LatLng(arr[1], arr[0]))
            }
            _polyline = _googleMap?.addPolyline(polylineOptions)
            _polyline?.isVisible = false
        }
        markersToData.keys.forEach { it.remove() }
        markersToData.clear()
        responseModel.track.forEach { trackItem ->
            val latLng = LatLng(trackItem.lat, trackItem.lon)
            val markerOptions =
                MarkerOptions().position(latLng)
            markersToData[_googleMap!!.addMarker(markerOptions)] = trackItem

        }
    }



    val markersToData
        get() = (requireActivity() as AssetActivity).markersToData

    override fun onDateRangeSelect(start: String, end: String) {
        presenter.getAssetTrackByTimeResponse(start, end)
    }

}