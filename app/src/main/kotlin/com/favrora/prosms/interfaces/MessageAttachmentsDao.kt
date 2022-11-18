package com.favrora.prosms.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.favrora.prosms.models.MessageAttachment

@Dao
interface MessageAttachmentsDao {
    @Query("SELECT * FROM message_attachments")
    fun getAll(): List<MessageAttachment>
}
