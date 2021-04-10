package com.cwod.trasset.asset.view

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_asset.*


class AssetTrackView : BaseFragment<TrackWrapper>(), OnMapReadyCallback,
    DateRangeSelector, GoogleMap.OnInfoWindowClickListener {
    companion object {
        const val TAG = "AssetsMapView"

    }

    lateinit var presenter: AssetTrackPresenter
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

    fun setPolygon(polygonOptions: PolygonOptions) {
        _polygon = _googleMap?.addPolygon(polygonOptions)
        _polygon?.isVisible = false
    }

    fun setPolyline(polylineOptions: PolylineOptions) {
        _polyline = _googleMap?.addPolyline(polylineOptions)
        _polyline?.isVisible = false
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
            setOnInfoWindowClickListener(this@AssetTrackView)
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

        setupMarkers(responseModel)

    }

    fun setupMarkers(responseModel: TrackWrapper) {
        markersToData.keys.forEach { it.remove() }
        markersToData.clear()
        responseModel.track.forEach { trackItem ->
            val markerOptions = MarkerOptions().position(LatLng(trackItem.lat, trackItem.lon))
            markersToData[_googleMap!!.addMarker(markerOptions)] = trackItem
        }
    }

    val markersToData
        get() = (requireActivity() as AssetActivity).markersToData

    override fun onDateRangeSelect(start: String, end: String) {
        presenter.getAssetTrackByTimeResponse(start, end)
    }


    override fun onInfoWindowClick(marker: Marker?) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val data = markersToData[marker]!!
        builder.setTitle("Show Location in Google Maps")
            .setMessage(presenter.getPopupMessage(data))
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                startActivity(presenter.getGoogleMapsIntent(data))
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
            }
        builder.create().show()
    }

    override fun onDestroyView() {
        if (this::presenter.isInitialized)
            presenter.onCleared()
        super.onDestroyView()
    }

}