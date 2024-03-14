package com.anthony.yapewhatsapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anthony.yapewhatsapp.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageModel)

    @Query("SELECT * FROM message_table")
    fun getAll():Flow<List<MessageModel>?>

    @Delete
    suspend fun delete(message: MessageModel)

    @Query("DELETE FROM message_table")
    fun deleteAll()

    @Query("SELECT * FROM message_table WHERE app_name IN (:apps) AND date>=:today")
    fun filterByApp(apps:List<String>,today:String):Flow<List<MessageModel>?>

    @Query("SELECT * FROM message_table WHERE app_name IN (:apps) AND date BETWEEN :start AND :end")
    fun filterByDate(apps:List<String>,start:String,end:String): Flow<List<MessageModel>>
}