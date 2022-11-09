package com.favrora.smsgram.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.favrora.commons.extensions.notificationManager
import com.favrora.commons.helpers.ensureBackgroundThread
import com.favrora.smsgram.extensions.conversationsDB
import com.favrora.smsgram.extensions.markThreadMessagesRead
import com.favrora.smsgram.extensions.updateUnreadCountBadge
import com.favrora.smsgram.helpers.MARK_AS_READ
import com.favrora.smsgram.helpers.THREAD_ID
import com.favrora.smsgram.helpers.refreshMessages

class MarkAsReadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            MARK_AS_READ -> {
                val threadId = intent.getLongExtra(THREAD_ID, 0L)
                context.notificationManager.cancel(threadId.hashCode())
                ensureBackgroundThread {
                    context.markThreadMessagesRead(threadId)
                    context.conversationsDB.markRead(threadId)
                    context.updateUnreadCountBadge(context.conversationsDB.getUnreadConversations())
                    refreshMessages()
                }
            }
        }
    }
}
