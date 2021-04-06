package com.cwod.trasset.asset.view

import android.app.AlertDialog
import android.content.Intent
import com.cwod.trasset.R
import com.cwod.trasset.asset.presenter.AssetTrackPresenter
import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.TrackItemModel
import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class AssetTrackView : BaseFragment<TrackWrapper>(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener, DateRangeSelector {
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
        val id = requireActivity().intent.getStringExtra("id")?:"none"
        presenter = AssetTrackPresenter(this, AssetTrackProvider(id))
        presenter.getAssetTrackResponse()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            setOnInfoWindowClickListener(this@AssetTrackView);
            setInfoWindowAdapter(Popup(requireContext(), markersToData))
            _googleMap = this
            requireActivity()
        }
    }

    override fun loadResponse(responseModel: TrackWrapper) {
        println(responseModel)
        show("" + responseModel.track.size)
        if (_googleMap==null) return;
        if (responseModel.track.isNotEmpty())
            _googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        responseModel.track[0].lat,
                        responseModel.track[0].lon
                    ), ZOOM_LEVEL
                )
            )


        responseModel.geoFence?.apply {
            var polygonOptions = PolygonOptions()
            geometry.coordinates?.get(0)?.forEach { arr ->
                polygonOptions = polygonOptions.add(LatLng(arr[1], arr[0]))
            }
            _polygon = _googleMap?.addPolygon(polygonOptions)
            _polygon?.isVisible = false
        }

        responseModel.geoRoute?.apply {
            var polylineOptions = PolylineOptions()
            geometry.coordinates?.forEach {arr ->
                polylineOptions =  polylineOptions.add(LatLng(arr[1],arr[0]))
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
            markersToData[_googleMap!!.addMarker(markerOptions)] = trackItem;

        }
    }

    override fun onInfoWindowClick(p0: Marker?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Track this asset")
            .setPositiveButton("Yes"
            ) { dialog, id ->
                val intent = Intent(context, AssetActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No"
            ) { dialog, id ->
            }
        builder.create().show()
    }

    val markersToData
        get() = (requireActivity() as AssetActivity).markersToData

    override fun onDateRangeSelect(start: String, end: String) {
        println("trackbytime "+start+" "+end)
        presenter.getAssetTrackByTimeResponse(start,end)
    }

}