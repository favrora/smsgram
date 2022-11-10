package com.favrora.smsgram.activities

import android.content.Intent
import com.simplemobiletools.commons.activities.BaseSplashActivity

class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        startActivity(Intent(this, com.favrora.smsgram.activities.MainActivity::class.java))
        finish()
    }
}
