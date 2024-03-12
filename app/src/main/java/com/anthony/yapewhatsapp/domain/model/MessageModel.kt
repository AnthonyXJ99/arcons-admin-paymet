package com.anthony.yapewhatsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class MessageModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "sub_message")
    val subMessage:String,

    @ColumnInfo(name = "app_name")
    val appName:String

)
