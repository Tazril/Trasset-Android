package com.cwod.trasset

import android.app.Application
import com.cwod.trasset.helper.DataFormatter
import com.google.android.gms.security.ProviderInstaller
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

class CollectApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DataFormatter.createInstance(this)
//        try {
//            // Google Play will install latest OpenSSL
//            ProviderInstaller.installIfNeeded(applicationContext);
//            val sslContext = SSLContext.getInstance("TLSv1.2");
//            sslContext.init(null, null, null);
//            sslContext.createSSLEngine();
//        } catch (e:Exception) {
//            e.printStackTrace();
//        }
    }
}