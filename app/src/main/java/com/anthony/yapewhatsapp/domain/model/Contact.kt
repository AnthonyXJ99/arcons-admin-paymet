package com.anthony.yapewhatsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    @ColumnInfo(name = "name_contact")
    val name:String,

    @ColumnInfo(name="number_contact")
    val number:String
)
