package com.anthony.yapewhatsapp.di

import android.content.Context
import androidx.room.Room
import com.anthony.yapewhatsapp.data.dao.ContactDao
import com.anthony.yapewhatsapp.data.dao.MessageDao
import com.anthony.yapewhatsapp.data.database.MessageDatabase
import com.anthony.yapewhatsapp.data.repository.ContactRepository
import com.anthony.yapewhatsapp.data.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): MessageDatabase {
        return  Room.databaseBuilder(context, MessageDatabase::class.java,"local_db").build()
    }

    @Provides
    @Singleton
    fun provideMessageDao(db: MessageDatabase):MessageDao=db.messageDao()


    @Provides
    @Singleton
    fun provideContactDao(db: MessageDatabase):ContactDao=db.contactDao()

    @Provides
    @Singleton
    fun provideMessageRepository(dao:MessageDao):MessageRepository= MessageRepository(dao)

    @Provides
    @Singleton
    fun provideContactRepository(dao:ContactDao):ContactRepository=ContactRepository(dao)
}