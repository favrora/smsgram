package com.favrora.smsgram.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.favrora.commons.extensions.showErrorToast
import com.favrora.commons.helpers.ensureBackgroundThread
import com.favrora.smsgram.R
import com.favrora.smsgram.extensions.conversationsDB
import com.favrora.smsgram.extensions.deleteScheduledMessage
import com.favrora.smsgram.extensions.getAddresses
import com.favrora.smsgram.extensions.messagesDB
import com.favrora.smsgram.helpers.SCHEDULED_MESSAGE_ID
import com.favrora.smsgram.helpers.THREAD_ID
import com.favrora.smsgram.helpers.refreshMessages
import com.favrora.smsgram.helpers.sendMessage

class ScheduledMessageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "simple.messenger:scheduled.message.receiver")
        wakelock.acquire(3000)


        ensureBackgroundThread {
            handleIntent(context, intent)
        }
    }

    private fun handleIntent(context: Context, intent: Intent) {
        val threadId = intent.getLongExtra(THREAD_ID, 0L)
        val messageId = intent.getLongExtra(SCHEDULED_MESSAGE_ID, 0L)
        val message = try {
            context.messagesDB.getScheduledMessageWithId(threadId, messageId)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        val addresses = message.participants.getAddresses()
        val attachments = message.attachment?.attachments ?: emptyList()

        try {
            context.sendMessage(message.body, addresses, message.subscriptionId, attachments)

            // delete temporary conversation and message as it's already persisted to the telephony db now
            context.deleteScheduledMessage(messageId)
            context.conversationsDB.deleteThreadId(messageId)
            refreshMessages()
        } catch (e: Exception) {
            context.showErrorToast(e)
        } catch (e: Error) {
            context.showErrorToast(e.localizedMessage ?: context.getString(R.string.unknown_error_occurred))
        }
    }
}
