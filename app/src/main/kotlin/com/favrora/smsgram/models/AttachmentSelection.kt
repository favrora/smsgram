package com.favrora.smsgram.models

import android.net.Uri
import com.favrora.smsgram.extensions.isImageMimeType
import com.favrora.smsgram.extensions.isVCardMimeType
import com.favrora.smsgram.extensions.isVideoMimeType
import com.favrora.smsgram.helpers.ATTACHMENT_DOCUMENT
import com.favrora.smsgram.helpers.ATTACHMENT_MEDIA
import com.favrora.smsgram.helpers.ATTACHMENT_VCARD

data class AttachmentSelection(
    val id: String,
    val uri: Uri,
    val mimetype: String,
    val filename: String,
    var isPending: Boolean,
    val viewType: Int = getViewTypeForMimeType(mimetype)
) {
    companion object {
        fun getViewTypeForMimeType(mimetype: String): Int {
            return when {
                mimetype.isImageMimeType() || mimetype.isVideoMimeType() -> ATTACHMENT_MEDIA
                mimetype.isVCardMimeType() -> ATTACHMENT_VCARD
                else -> ATTACHMENT_DOCUMENT
            }
        }

        fun areItemsTheSame(first: AttachmentSelection, second: AttachmentSelection): Boolean {
            return first.id == second.id
        }

        fun areContentsTheSame(first: AttachmentSelection, second: AttachmentSelection): Boolean {
            return first.uri == second.uri && first.mimetype == second.mimetype && first.filename == second.filename
        }
    }
}
