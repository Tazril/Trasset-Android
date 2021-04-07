package com.cwod.trasset

import android.app.Application
import com.cwod.trasset.helper.DataFormatter

class CollectApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DataFormatter.createInstance(this)
    }
}