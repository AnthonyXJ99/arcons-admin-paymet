package com.anthony.yapewhatsapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anthony.yapewhatsapp.domain.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM contact_table")
    fun getAll(): Flow<List<Contact>?>

    @Delete
    suspend fun delete(contact: Contact)

    @Query("DELETE FROM contact_table")
    fun deleteAll()
}