package com.anthony.yapewhatsapp.data.repository

import com.anthony.yapewhatsapp.data.dao.ContactDao
import com.anthony.yapewhatsapp.domain.model.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: ContactDao) {

    suspend fun insert(contact: Contact) =dao.insert(contact)

    fun getAll(): Flow<List<Contact>?> {
        return dao.getAll()
    }

    suspend fun delete(contact: Contact) = dao.delete(contact)

    fun deleteAll() =dao.deleteAll()
}