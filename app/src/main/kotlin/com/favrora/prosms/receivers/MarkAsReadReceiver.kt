package com.favrora.prosms.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.favrora.commons.extensions.notificationManager
import com.favrora.commons.helpers.ensureBackgroundThread
import com.favrora.prosms.extensions.conversationsDB
import com.favrora.prosms.extensions.markThreadMessagesRead
import com.favrora.prosms.extensions.updateUnreadCountBadge
import com.favrora.prosms.helpers.MARK_AS_READ
import com.favrora.prosms.helpers.THREAD_ID
import com.favrora.prosms.helpers.refreshMessages

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
