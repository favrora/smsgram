package com.favrora.smsgram.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.favrora.smsgram.models.Attachment

@Dao
interface AttachmentsDao {
    @Query("SELECT * FROM attachments")
    fun getAll(): List<Attachment>
}
