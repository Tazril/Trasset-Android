package com.cwod.trasset.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity(val layoutId: Int) : FragmentActivity() {

    abstract var progressBar: ProgressBar
    abstract var toolbar: Toolbar?


    // To initilaize in OnCreate method of activity
    abstract fun initActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initActivity()
    }

    // Toast messages
    infix fun show(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    infix fun showLong(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    infix fun showError(message: String) =
        Snackbar.make(progressBar, message, Snackbar.LENGTH_SHORT).show()

    // extending view class function

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.INVISIBLE
    }


    // check internet connection
    fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.activeNetwork != null
        } else {
            show("Your device version is not compatible")
            return false
        }  //can also be null in airplane mode
    }

    fun hideKeyboard() {
        currentFocus.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it?.windowToken, 0)
        }
    }
}