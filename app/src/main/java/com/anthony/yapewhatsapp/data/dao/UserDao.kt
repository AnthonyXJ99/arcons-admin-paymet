package com.anthony.yapewhatsapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anthony.yapewhatsapp.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user:UserEntity)

    @Query("SELECT * FROM user ORDER BY id ASC LIMIT 1")
    fun findUser():Flow<UserEntity>

    @Update
    suspend fun update(user:UserEntity)
}