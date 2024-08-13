package com.example.quantyc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quantyc.data.local.entities.RemoteKeysEntity

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    @Query("Select * From remote_key Where photo_id = :photoId")
    suspend fun getRemoteKeyByPhotoID(photoId: Int): RemoteKeysEntity?

    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From remote_key Order By created_at DESC Limit 1")
    suspend fun getCreationTime(): Long?
}