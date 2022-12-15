package com.favrora.prosms.activities

import android.content.Intent
import com.favrora.commons.activities.BaseSplashActivity
import com.favrora.prosms.helpers.Preference

class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        if (Preference(this).IsAgreementAccept())
        {
            val intent=Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        else
        {
            startActivity(Intent(this,AgreementA::class.java))
        }
    }
}
