package com.cwod.trasset.home.view

import android.app.AlertDialog
import android.content.Intent
import androidx.core.os.bundleOf
import com.cwod.trasset.R
import com.cwod.trasset.asset.view.AssetActivity
import com.cwod.trasset.base.BaseFragment
import com.cwod.trasset.home.presenter.AssetListPresenter
import com.cwod.trasset.home.provider.AssetListProvider
import com.cwod.trasset.home.provider.model.AssetListModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class AssetsMapView : BaseFragment<List<AssetListModel>>(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener {
    companion object {
        const val TAG = "AssetsMapView"

    }

    lateinit var presenter: AssetListPresenter
    val ZOOM_LEVEL = 8f
    val markersToData = mutableMapOf<Marker, AssetListModel>();

    override val layoutId: Int = R.layout.fragment_assets

    private lateinit var _googleMap: GoogleMap

    override fun initView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter = AssetListPresenter(this, AssetListProvider())
        presenter.getAssetListResponse()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            setOnInfoWindowClickListener(this@AssetsMapView);
            setInfoWindowAdapter(Popup(requireContext(), markersToData))
            _googleMap = this
        }
    }

    override fun loadResponse(responseModel: List<AssetListModel>) {
        if (!this::_googleMap.isInitialized) return;
        if (responseModel.isNotEmpty())
            _googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        responseModel[0].lat,
                        responseModel[0].lon
                    ), ZOOM_LEVEL
                )
            )
        responseModel.forEach { asset ->
            val latLng = LatLng(asset.lat, asset.lon)
            val markerOptions =
                MarkerOptions().position(latLng).title(asset.name).snippet(asset.desc)
            markersToData[_googleMap.addMarker(markerOptions)] = asset;

        }
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Track this asset")
            .setPositiveButton("Yes"
            ) { dialog, id ->
                val intent = Intent(context, AssetActivity::class.java).apply {
                    putExtra("id", markersToData[marker]?._id)
                }
                startActivity(intent)
            }
            .setNegativeButton("No"
            ) { dialog, id ->
            }
        builder.create().show()
    }

}