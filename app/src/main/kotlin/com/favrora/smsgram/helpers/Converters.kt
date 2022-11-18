package com.favrora.smsgram.helpers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.favrora.commons.models.SimpleContact
import com.favrora.smsgram.models.Attachment
import com.favrora.smsgram.models.MessageAttachment

class Converters {
    private val gson = Gson()
    private val attachmentType = object : TypeToken<List<Attachment>>() {}.type
    private val simpleContactType = object : TypeToken<List<SimpleContact>>() {}.type
    private val messageAttachmentType = object : TypeToken<MessageAttachment?>() {}.type

    @TypeConverter
    fun jsonToAttachmentList(value: String) = gson.fromJson<ArrayList<Attachment>>(value, attachmentType)

    @TypeConverter
    fun attachmentListToJson(list: ArrayList<Attachment>) = gson.toJson(list)

    @TypeConverter
    fun jsonToSimpleContactList(value: String) = gson.fromJson<ArrayList<SimpleContact>>(value, simpleContactType)

    @TypeConverter
    fun simpleContactListToJson(list: ArrayList<SimpleContact>) = gson.toJson(list)

    @TypeConverter
    fun jsonToMessageAttachment(value: String) = gson.fromJson<MessageAttachment>(value, messageAttachmentType)

    @TypeConverter
    fun messageAttachmentToJson(messageAttachment: MessageAttachment?) = gson.toJson(messageAttachment)
}
