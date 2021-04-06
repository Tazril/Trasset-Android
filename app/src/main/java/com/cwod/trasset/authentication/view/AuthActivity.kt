package com.cwod.trasset.authentication.view

import android.content.Intent
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cwod.trasset.R
import com.cwod.trasset.base.BaseActivity
import com.cwod.trasset.helper.*
import com.cwod.trasset.home.view.HomeActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity(R.layout.activity_auth) {

    override lateinit  var progressBar: ProgressBar
    override var swipeRefreshLayout: SwipeRefreshLayout? = null
    override var toolbar: Toolbar? = null
    lateinit var navController: NavController


    override fun initActivity() {
        SharedPref.instantiate(this)
        progressBar = progressBarMain
        swipeRefreshLayout = swipeRefreshLayoutMain
        navController = findNavController(R.id.fragmentHostMain)
        val accessToken:String? = SharedPref.getString(Constants.AUTHORIZATION)

        accessToken?.let{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
