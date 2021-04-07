package com.cwod.trasset.home.view

import android.content.Intent
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
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
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_asset_list.*


class AssetListMapView : BaseFragment<List<AssetListModel>>(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener, AssetTypeSelector {
    companion object {
        const val TAG = "AssetsMapView"

    }

    lateinit var presenter: AssetListPresenter
    val ZOOM_LEVEL = 3f
    val FOCUS_ZOOM_LEVEL = 8f
    val markersToData = mutableMapOf<Marker, AssetListModel>()

    override val layoutId: Int = R.layout.fragment_asset_list

    private lateinit var _googleMap: GoogleMap

    override fun initView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter = AssetListPresenter(this, AssetListProvider())
        presenter.getAssetListResponse("")

    }


    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            setOnInfoWindowClickListener(this@AssetListMapView)
            setInfoWindowAdapter(Popup(requireContext(), markersToData))
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    25.0,
                    74.0
                ), ZOOM_LEVEL
            )
            _googleMap = this
        }
    }

    override fun loadResponse(responseModel: List<AssetListModel>) {
        if (!this::_googleMap.isInitialized) return
        if (responseModel.isNotEmpty())
            _googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        responseModel[0].lat,
                        responseModel[0].lon
                    ), ZOOM_LEVEL
                )
            )

        val arr = responseModel.map { it.name }
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_selectable_list_item, arr)
        requireActivity().autoComplete.threshold = 1
        requireActivity().autoComplete.setAdapter(adapter)
        requireActivity().autoComplete.setOnItemClickListener { parent, view, position, id ->
            _googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        responseModel[position].lat,
                        responseModel[position].lon
                    ), FOCUS_ZOOM_LEVEL
                )
            )
        }

        markersToData.keys.forEach { it.remove() }
        markersToData.clear()

        responseModel.forEach { asset ->
            val latLng = LatLng(asset.lat, asset.lon)
            val pickupMarkerDrawable = ContextCompat.getDrawable(
                requireContext(),
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
            val markerOptions =
                MarkerOptions().position(latLng).title(asset.name).snippet(asset.desc)
                    .icon(icon)
            markersToData[_googleMap.addMarker(markerOptions)] = asset
        }
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val icon = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_location
        )
        builder.setTitle(marker?.title)
            .setIcon(icon)
            .setMessage("Get a timeline view of the asset")
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                val intent = Intent(context, AssetActivity::class.java).apply {
                    putExtra("id", markersToData[marker]?._id)
                }
                startActivity(intent)
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
            }
        builder.create().show()
    }

    override fun onTypeSelect(assetType: String) {
        presenter.getAssetListResponse(assetType)
    }

}