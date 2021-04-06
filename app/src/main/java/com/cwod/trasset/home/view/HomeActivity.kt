package com.cwod.trasset.home.view

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cwod.trasset.R
import com.cwod.trasset.asset.view.AssetTrackDialog
import com.cwod.trasset.base.BaseActivity
import com.cwod.trasset.helper.ApiClient
import com.cwod.trasset.helper.Constants
import com.cwod.trasset.helper.SharedPref
import com.cwod.trasset.helper.Urls
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(R.layout.activity_home) {

    override lateinit var progressBar: ProgressBar
    override var swipeRefreshLayout: SwipeRefreshLayout? = null
    override var toolbar: Toolbar? = null
    lateinit var navController: NavController
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initActivity() {

        SharedPref.instantiate(this)
        val accessToken: String? = SharedPref.getString(Constants.AUTHORIZATION)
        var email: String? = SharedPref.getString(Constants.EMAIL)
        ApiClient.instantiateWithAccessToken(this, accessToken)
        progressBar = progressBarHome
//        swipeRefreshLayout = swipeRefreshLayoutHome
//        swipeRefreshLayout?.setColorSchemeColors(Color.BLUE, Color.GREEN)
//        navController = findNavController(R.id.fragmentHostHome)
//        setupActionBarWithNavController(navController)
        setUpBottomNavigation()
//        NavigationUI.setupActionBarWithNavController(this, navController)
        sessionNewFAB.setOnClickListener {
//            AssetTrackDialog().show(supportFragmentManager, AssetTrackDialog.TAG)
        }
    }

    private fun setUpBottomNavigation() {
        title = "Active Sessions"
        showSessionList()
        bottomNavHome.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.AssetsMapView -> {
                        swipeRefreshLayout?.isEnabled = true
                        title = "Active Sessions"
                        showSessionList()
                        true
                    }
                    R.id.dashboardView -> {
                        swipeRefreshLayout?.isEnabled = false
                        title = "DashBoard"
                        showDashboard()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showSessionList() {
        if (activeFragment is AssetsMapView) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(AssetsMapView.TAG) as AssetsMapView?

        if (fragment == null) {
            fragment = AssetsMapView()
            fragmentTransaction.add(R.id.fragmentHostHome, fragment, AssetsMapView.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment

        return
    }

    private fun showDashboard() {
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


}
