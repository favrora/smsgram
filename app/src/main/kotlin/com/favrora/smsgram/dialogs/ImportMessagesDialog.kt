package com.favrora.smsgram.dialogs

import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.favrora.commons.extensions.getAlertDialogBuilder
import com.favrora.commons.extensions.setupDialogStuff
import com.favrora.commons.extensions.toast
import com.favrora.commons.helpers.ensureBackgroundThread
import com.favrora.smsgram.R
import com.favrora.smsgram.activities.SimpleActivity
import com.favrora.smsgram.extensions.config
import com.favrora.smsgram.helpers.MessagesImporter
import com.favrora.smsgram.helpers.MessagesImporter.ImportResult.IMPORT_OK
import com.favrora.smsgram.helpers.MessagesImporter.ImportResult.IMPORT_PARTIAL
import kotlinx.android.synthetic.main.dialog_import_messages.view.*

class ImportMessagesDialog(
    private val activity: SimpleActivity,
    private val path: String,
) {

    private val config = activity.config

    init {
        var ignoreClicks = false
        val view = (activity.layoutInflater.inflate(R.layout.dialog_import_messages, null) as ViewGroup).apply {
            import_sms_checkbox.isChecked = config.importSms
            import_mms_checkbox.isChecked = config.importMms
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(view, this, R.string.import_messages) { alertDialog ->
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        if (ignoreClicks) {
                            return@setOnClickListener
                        }

                        if (!view.import_sms_checkbox.isChecked && !view.import_mms_checkbox.isChecked) {
                            activity.toast(R.string.no_option_selected)
                            return@setOnClickListener
                        }

                        ignoreClicks = true
                        activity.toast(R.string.importing)
                        config.importSms = view.import_sms_checkbox.isChecked
                        config.importMms = view.import_mms_checkbox.isChecked
                        ensureBackgroundThread {
                            MessagesImporter(activity).importMessages(path) {
                                handleParseResult(it)
                                alertDialog.dismiss()
                            }
                        }
                    }
                }
            }
    }

    private fun handleParseResult(result: MessagesImporter.ImportResult) {
        activity.toast(
            when (result) {
                IMPORT_OK -> R.string.importing_successful
                IMPORT_PARTIAL -> R.string.importing_some_entries_failed
                else -> R.string.no_items_found
            }
        )
    }
}
