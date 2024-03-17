package com.anthony.yapewhatsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,

    @ColumnInfo(name = "email")
    val email:String,

    @ColumnInfo(name="mac")
    val mac:String,

    @ColumnInfo(name="name")
    val name:String,

    @ColumnInfo("phone")
    val phone:String,

    @ColumnInfo(name="isActive")
    var isActive:Boolean=false
)

