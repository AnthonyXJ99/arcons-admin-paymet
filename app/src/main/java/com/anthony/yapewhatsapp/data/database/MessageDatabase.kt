package com.anthony.yapewhatsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anthony.yapewhatsapp.data.dao.ContactDao
import com.anthony.yapewhatsapp.data.dao.MessageDao
import com.anthony.yapewhatsapp.data.dao.UserDao
import com.anthony.yapewhatsapp.domain.model.Contact
import com.anthony.yapewhatsapp.domain.model.MessageModel
import com.anthony.yapewhatsapp.domain.model.UserEntity

@Database(entities = [MessageModel::class,Contact::class,UserEntity::class], version = 1, exportSchema = true)
abstract class MessageDatabase:RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun contactDao(): ContactDao

    abstract fun userDao():UserDao

}