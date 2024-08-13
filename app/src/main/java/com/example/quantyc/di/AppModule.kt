package com.example.quantyc.di

import android.content.Context
import androidx.room.Room
import com.example.quantyc.data.local.dao.PhotoDao
import com.example.quantyc.data.local.PhotoDatabase
import com.example.quantyc.data.local.dao.RemoteKeysDao
import com.example.quantyc.data.local.dao.UserDao
import com.example.quantyc.data.mapper.PhotoMapper
import com.example.quantyc.data.remote.PhotoApiService
import com.example.quantyc.data.repository.AuthRepositoryImpl
import com.example.quantyc.data.repository.PhotoRepositoryImpl
import com.example.quantyc.domain.repository.AuthRepository
import com.example.quantyc.domain.repository.PhotoRepository
import com.example.quantyc.domain.usecase.GetPhotoUseCase
import com.example.quantyc.domain.usecase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase =
        Room.databaseBuilder(context, PhotoDatabase::class.java, "photos_database")
            .build()

    @Singleton
    @Provides
    fun providePhotosDao(photosDatabase: PhotoDatabase): PhotoDao = photosDatabase.getPhotoDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(photosDatabase: PhotoDatabase): RemoteKeysDao = photosDatabase.getRemoteKeysDao()

    @Singleton
    @Provides
    fun provideUserDao(photosDatabase: PhotoDatabase): UserDao = photosDatabase.getUserDao()

    @Singleton
    @Provides
    fun providePhotoRepository(
        photoApiService: PhotoApiService,
        photoDatabase: PhotoDatabase,
        photoMapper: PhotoMapper
    ): PhotoRepository = PhotoRepositoryImpl(photoApiService, photoDatabase, photoMapper)

    @Provides
    @Singleton
    fun provideAuthRepository(photosDatabase: PhotoDatabase): AuthRepository {
        return AuthRepositoryImpl(photosDatabase)
    }

    @Singleton
    @Provides
    fun providePhotoMapper(): PhotoMapper = PhotoMapper()

    @Singleton
    @Provides
    fun provideGetPopularPhotosUseCase(repository: PhotoRepository): GetPhotoUseCase =
        GetPhotoUseCase(repository)

    @Singleton
    @Provides
    fun provideUserUseCase(repository: AuthRepository): UserUseCase =
        UserUseCase(repository)
}