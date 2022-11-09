package com.favrora.smsgram

import android.app.Application
import com.favrora.commons.extensions.checkUseEnglish

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
    }
}
