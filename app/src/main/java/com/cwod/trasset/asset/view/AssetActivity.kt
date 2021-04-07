package com.cwod.trasset.asset.view

import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cwod.trasset.R
import com.cwod.trasset.asset.presenter.NotificationListPresenter
import com.cwod.trasset.asset.provider.NotificationListProvider
import com.cwod.trasset.asset.provider.model.TrackItemModel
import com.cwod.trasset.base.BaseActivity
import com.cwod.trasset.common.Notification
import com.cwod.trasset.common.NotificationAdapter
import com.cwod.trasset.home.provider.model.NotificationModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_asset.*

class AssetActivity : BaseActivity(R.layout.activity_asset), Notification {

    override lateinit var progressBar: ProgressBar
    override var toolbar: Toolbar? = null
    val markersToData = mutableMapOf<Marker, TrackItemModel>()
    var dateRange = Pair<Long, Long>(0, 0)
    var isDateRange = false
    var _polygon: Polygon? = null
    var _polyline: Polyline? = null
    var _googleMap: GoogleMap? = null
    lateinit var notificationListPresenter: NotificationListPresenter
    val adapter = NotificationAdapter()

    override fun initActivity() {
        progressBar = progressBarHome
        setUpBottomNavigation()
        sessionNewFAB.setOnClickListener {
            AssetTrackDialog(
                if (activeFragment is AssetTrackView) activeFragment as AssetTrackView else
                    supportFragmentManager.findFragmentByTag(AssetTrackView.TAG) as AssetTrackView?
            ).show(
                supportFragmentManager,
                AssetTrackDialog.TAG
            )
        }

        val sheetBehavior = BottomSheetBehavior.from(contentLinearLayout)
        sheetBehavior.isFitToContents = false
        sheetBehavior.isHideable = false //prevents the bottom sheet from completely hiding off the screen
        sheetBehavior.isDraggable = false //prevents user drag
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED //initially state to fully expanded

        notificationIcon.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }

        backIcon.setOnClickListener {
            finish()
        }

        recyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = adapter

        val id = intent.getStringExtra("id") ?: "none"
        notificationListPresenter = NotificationListPresenter(this, NotificationListProvider())
        notificationListPresenter.getNotificationListResponse(id)


    }

    private var activeFragment: Fragment? = null

    private fun setUpBottomNavigation() {
        title = "Active Sessions"
        showAssetTrack()
        bottomNavHome.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.AssetTrackView -> {
                        title = "Asset"
                        showAssetTrack()
                        true
                    }
                    R.id.AssetInfoView -> {
                        title = "Info"
                        showAssetInfo()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showAssetTrack() {
        if (activeFragment is AssetTrackView) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(AssetTrackView.TAG) as AssetTrackView?

        if (fragment == null) {
            fragment = AssetTrackView()
            fragmentTransaction.add(R.id.fragmentHostHome, fragment, AssetTrackView.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment

        return
    }

    private fun showAssetInfo() {
        if (activeFragment is AssetInfoView) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(AssetInfoView.TAG) as AssetInfoView?

        if (fragment == null) {
            fragment = AssetInfoView()
            fragmentTransaction.add(R.id.fragmentHostHome, fragment, AssetInfoView.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment

        return
    }

    override fun onNotificationsRecieved(notifications: List<NotificationModel>) {
        adapter.list = notifications
        adapter.notifyDataSetChanged()
    }
}