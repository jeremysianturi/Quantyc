package com.example.quantyc.di

import com.example.quantyc.data.local.dao.PhotoDao
import com.example.quantyc.data.local.PhotoDatabase
import com.example.quantyc.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePhotoDao(photoDatabase: PhotoDatabase): PhotoDao = photoDatabase.getPhotoDao()

    @Provides
    @Singleton
    fun provideUserDao(photoDatabase: PhotoDatabase): UserDao = photoDatabase.getUserDao()
}