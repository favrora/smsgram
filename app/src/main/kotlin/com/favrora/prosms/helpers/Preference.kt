package com.favrora.prosms.helpers

import android.content.Context
import android.content.SharedPreferences
import com.favrora.prosms.R

class Preference(ctx: Context){

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val KEY_IS_AGREEMENT_ACCEPT = "is_Agreement_Accept"

    init {
        prefs = ctx.getSharedPreferences("${ctx.getString(R.string.app_name)}", Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun IsAgreementAccept(): Boolean {
        return prefs.getBoolean(KEY_IS_AGREEMENT_ACCEPT, false)
    }

    fun setAgreementAccept(agreementAccept: Boolean) {
        editor.putBoolean(KEY_IS_AGREEMENT_ACCEPT, agreementAccept)
        editor.commit()
    }



}
