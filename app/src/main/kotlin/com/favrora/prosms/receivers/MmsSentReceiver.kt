package com.favrora.prosms.receivers

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.favrora.prosms.helpers.refreshMessages

class MmsSentReceiver : com.klinker.android.send_message.MmsSentReceiver() {
    override fun onMessageStatusUpdated(context: Context?, intent: Intent?, resultCode: Int) {
        super.onMessageStatusUpdated(context, intent, resultCode)
        if (resultCode == Activity.RESULT_OK) {
            refreshMessages()
        }
    }
}
