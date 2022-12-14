package com.favrora.prosms.activities

import com.favrora.commons.activities.BaseSimpleActivity
import com.favrora.prosms.R

open class SimpleActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(
        R.mipmap.ic_launcher
    )

    override fun getAppLauncherName() = getString(R.string.appNewName)
}