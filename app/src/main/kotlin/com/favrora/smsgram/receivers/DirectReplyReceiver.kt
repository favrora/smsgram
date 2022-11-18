package com.favrora.smsgram.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import com.klinker.android.send_message.Transaction
import com.favrora.commons.extensions.notificationManager
import com.favrora.commons.extensions.showErrorToast
import com.favrora.commons.helpers.ensureBackgroundThread
import com.favrora.smsgram.extensions.*
import com.favrora.smsgram.helpers.REPLY
import com.favrora.smsgram.helpers.THREAD_ID
import com.favrora.smsgram.helpers.THREAD_NUMBER
import com.favrora.smsgram.helpers.getSendMessageSettings

class DirectReplyReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val address = intent.getStringExtra(THREAD_NUMBER)
        val threadId = intent.getLongExtra(THREAD_ID, 0L)
        var msg = RemoteInput.getResultsFromIntent(intent)?.getCharSequence(REPLY)?.toString() ?: return

        msg = context.removeDiacriticsIfNeeded(msg)

        val settings = context.getSendMessageSettings()
        if (address != null) {
            val availableSIMs = context.subscriptionManagerCompat().activeSubscriptionInfoList
            if ((availableSIMs?.size ?: 0) > 1) {
                val currentSIMCardIndex = context.config.getUseSIMIdAtNumber(address)
                val wantedId = availableSIMs.getOrNull(currentSIMCardIndex)
                if (wantedId != null) {
                    settings.subscriptionId = wantedId.subscriptionId
                }
            }
        }

        val transaction = Transaction(context, settings)
        val message = com.klinker.android.send_message.Message(msg, address)

        ensureBackgroundThread {
            try {
                val smsSentIntent = Intent(context, SmsStatusSentReceiver::class.java)
                val deliveredIntent = Intent(context, SmsStatusDeliveredReceiver::class.java)

                transaction.setExplicitBroadcastForSentSms(smsSentIntent)
                transaction.setExplicitBroadcastForDeliveredSms(deliveredIntent)

                transaction.sendNewMessage(message)
            } catch (e: Exception) {
                context.showErrorToast(e)
            }

            context.notificationManager.cancel(threadId.hashCode())

            context.markThreadMessagesRead(threadId)
            context.conversationsDB.markRead(threadId)
        }
    }
}
