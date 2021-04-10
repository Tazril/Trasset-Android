package com.cwod.trasset

import android.app.Application
import com.cwod.trasset.helper.DataFormatter

class TrassetApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DataFormatter.createInstance(this)
    }
}