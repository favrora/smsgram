package com.favrora.prosms.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.favrora.prosms.models.Attachment

@Dao
interface AttachmentsDao {
    @Query("SELECT * FROM attachments")
    fun getAll(): List<Attachment>
}
