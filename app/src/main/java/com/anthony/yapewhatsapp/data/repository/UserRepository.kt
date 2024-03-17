package com.anthony.yapewhatsapp.data.repository

import com.anthony.yapewhatsapp.data.dao.UserDao
import com.anthony.yapewhatsapp.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao:UserDao) {
    suspend fun insert(user: UserEntity)= dao.insert(user)
    fun  findUser(): Flow<UserEntity> = dao.findUser()
    suspend fun update(user: UserEntity) =dao.update(user)
}