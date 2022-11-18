package com.favrora.prosms.activities

import android.content.Intent
import com.favrora.commons.activities.BaseSplashActivity

class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        startActivity(Intent(this, com.favrora.prosms.activities.MainActivity::class.java))
        finish()
    }
}
