package com.example.quantyc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quantyc.data.local.dao.PhotoDao
import com.example.quantyc.data.local.dao.RemoteKeysDao
import com.example.quantyc.data.local.dao.UserDao
import com.example.quantyc.data.local.entities.PhotoEntity
import com.example.quantyc.data.local.entities.RemoteKeysEntity
import com.example.quantyc.data.local.entities.UserEntity

@Database(entities = [PhotoEntity::class, RemoteKeysEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun getPhotoDao(): PhotoDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
    abstract fun getUserDao(): UserDao
}