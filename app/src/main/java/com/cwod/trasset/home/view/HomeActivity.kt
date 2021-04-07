package com.cwod.trasset.home.view

import android.view.MotionEvent
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cwod.trasset.R
import com.cwod.trasset.asset.view.AssetTrackView
import com.cwod.trasset.base.BaseActivity
import com.cwod.trasset.common.Notification
import com.cwod.trasset.common.NotificationAdapter
import com.cwod.trasset.helper.ApiClient
import com.cwod.trasset.helper.Constants
import com.cwod.trasset.helper.SharedPref
import com.cwod.trasset.home.presenter.NotificationListPresenter
import com.cwod.trasset.home.provider.NotificationListProvider
import com.cwod.trasset.home.provider.model.NotificationModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(R.layout.activity_home), Notification {

    override lateinit var progressBar: ProgressBar
    override var toolbar: Toolbar? = null
    private var activeFragment: Fragment? = null
    var assetType = ""
    lateinit var notificationListPresenter: NotificationListPresenter

    val adapter = NotificationAdapter()

    override fun initActivity() {


        SharedPref.instantiate(this)
        val accessToken: String? = SharedPref.getString(Constants.AUTHORIZATION)
//        var email: String? = SharedPref.getString(Constants.EMAIL)
        ApiClient.instantiateWithAccessToken(this, accessToken)
        progressBar = progressBarHome

        recyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = adapter

        setUpBottomNavigation()
        sessionNewFAB.setOnClickListener {
            AssetListDialog(
                if (activeFragment is AssetListMapView) activeFragment as AssetListMapView else
                    supportFragmentManager.findFragmentByTag(AssetTrackView.TAG) as AssetListMapView?
            ).show(supportFragmentManager, AssetListDialog.TAG)
        }


        val sheetBehavior = BottomSheetBehavior.from(contentLinearLayout)
        sheetBehavior.isFitToContents = false
        sheetBehavior.isHideable = false //prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.isDraggable = false
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED //initially state to fully expanded

        notificationIcon.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }

        notificationListPresenter = NotificationListPresenter(this, NotificationListProvider())
        notificationListPresenter.getNotificationListResponse()
    }

    private fun setUpBottomNavigation() {
        title = "Active Sessions"
        showAssetList()
        name_text_input.show()
        bottomNavHome.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.AssetsMapView -> {
                        title = "Asset List"
                        showAssetList()
                        name_text_input.show()
                        true
                    }
                    R.id.dashboardView -> {
                        title = "Profile"
                        showProfile()
                        name_text_input.hide()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showAssetList() {
        if (activeFragment is AssetListMapView) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(AssetListMapView.TAG) as AssetListMapView?

        if (fragment == null) {
            fragment = AssetListMapView()
            fragmentTransaction.add(R.id.fragmentHostHome, fragment, AssetListMapView.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment

        return
    }

    private fun showProfile() {
        if (activeFragment is ProfileView) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(ProfileView.TAG) as ProfileView?

        if (fragment == null) {
            fragment = ProfileView()
            fragmentTransaction.add(R.id.fragmentHostHome, fragment, ProfileView.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment

        return
    }

    override fun onNotificationsRecieved(notifications: List<NotificationModel>) {
        println(notifications)
        adapter.list = notifications
        adapter.notifyDataSetChanged()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is AutoCompleteTextView) {
                hideKeyboard()
                activity_main.requestFocus()
            }
        }
        return super.dispatchTouchEvent(event)
    }


}
