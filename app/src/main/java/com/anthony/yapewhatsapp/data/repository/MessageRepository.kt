package com.anthony.yapewhatsapp.data.repository

import com.anthony.yapewhatsapp.data.dao.MessageDao
import com.anthony.yapewhatsapp.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val dao: MessageDao) {

    suspend fun insert(message: MessageModel) =dao.insert(message)

    fun getAll(): Flow<List<MessageModel>?> {
        return dao.getAll()
    }

    suspend fun delete(message: MessageModel) = dao.delete(message)

    fun deleteAll() =dao.deleteAll()

    fun filterByApp(apps:List<String>,today:String)=dao.filterByApp(apps,today)
}