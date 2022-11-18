package com.favrora.smsgram.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.AlarmManagerCompat
import com.klinker.android.send_message.Settings
import com.klinker.android.send_message.Transaction
import com.klinker.android.send_message.Utils
import com.favrora.commons.extensions.showErrorToast
import com.favrora.commons.helpers.isMarshmallowPlus
import com.favrora.smsgram.R
import com.favrora.smsgram.extensions.config
import com.favrora.smsgram.extensions.isPlainTextMimeType
import com.favrora.smsgram.models.Attachment
import com.favrora.smsgram.models.Message
import com.favrora.smsgram.receivers.ScheduledMessageReceiver
import com.favrora.smsgram.receivers.SmsStatusDeliveredReceiver
import com.favrora.smsgram.receivers.SmsStatusSentReceiver
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import kotlin.math.abs
import kotlin.random.Random

fun Context.getSendMessageSettings(): Settings {
    val settings = Settings()
    settings.useSystemSending = true
    settings.deliveryReports = config.enableDeliveryReports
    settings.sendLongAsMms = config.sendLongMessageMMS
    settings.sendLongAsMmsAfter = 1
    settings.group = config.sendGroupMessageMMS
    return settings
}

fun Context.sendMessage(text: String, addresses: List<String>, subscriptionId: Int?, attachments: List<Attachment>) {
    val settings = getSendMessageSettings()
    if (subscriptionId != null) {
        settings.subscriptionId = subscriptionId
    }

    val transaction = Transaction(this, settings)
    val message = com.klinker.android.send_message.Message(text, addresses.toTypedArray())

    if (attachments.isNotEmpty()) {
        for (attachment in attachments) {
            try {
                val uri = attachment.getUri()
                contentResolver.openInputStream(uri)?.use {
                    val bytes = it.readBytes()
                    val mimeType = if (attachment.mimetype.isPlainTextMimeType()) {
                        "application/txt"
                    } else {
                        attachment.mimetype
                    }
                    val name = attachment.filename
                    message.addMedia(bytes, mimeType, name, name)
                }
            } catch (e: Exception) {
                showErrorToast(e)
            } catch (e: Error) {
                showErrorToast(e.localizedMessage ?: getString(R.string.unknown_error_occurred))
            }
        }
    }

    val smsSentIntent = Intent(this, SmsStatusSentReceiver::class.java)
    val deliveredIntent = Intent(this, SmsStatusDeliveredReceiver::class.java)

    transaction.setExplicitBroadcastForSentSms(smsSentIntent)
    transaction.setExplicitBroadcastForDeliveredSms(deliveredIntent)
    Handler(Looper.getMainLooper()).post {
        transaction.sendNewMessage(message)
    }
}

fun Context.getScheduleSendPendingIntent(message: Message): PendingIntent {
    val intent = Intent(this, ScheduledMessageReceiver::class.java)
    intent.putExtra(THREAD_ID, message.threadId)
    intent.putExtra(SCHEDULED_MESSAGE_ID, message.id)

    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    if (isMarshmallowPlus()) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    return PendingIntent.getBroadcast(this, message.id.toInt(), intent, flags)
}

fun Context.scheduleMessage(message: Message) {
    val pendingIntent = getScheduleSendPendingIntent(message)
    val triggerAtMillis = message.millis()

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
}

fun Context.cancelScheduleSendPendingIntent(messageId: Long) {
    val intent = Intent(this, ScheduledMessageReceiver::class.java)
    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    if (isMarshmallowPlus()) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    PendingIntent.getBroadcast(this, messageId.toInt(), intent, flags).cancel()
}

fun Context.isLongMmsMessage(text: String): Boolean {
    val settings = getSendMessageSettings()
    return Utils.getNumPages(settings, text) > settings.sendLongAsMmsAfter
}

/** Not to be used with real messages persisted in the telephony db. This is for internal use only (e.g. scheduled messages). */
fun generateRandomId(length: Int = 9): Long {
    val millis = DateTime.now(DateTimeZone.UTC).millis
    val random = abs(Random(millis).nextLong())
    return random.toString().takeLast(length).toLong()
}
