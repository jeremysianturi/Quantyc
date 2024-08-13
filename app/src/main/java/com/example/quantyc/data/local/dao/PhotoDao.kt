package com.example.quantyc.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quantyc.data.local.entities.PhotoEntity

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos WHERE title LIKE :query")
    fun getPhotos(query: String): PagingSource<Int, PhotoEntity>

    @Query("SELECT * FROM photos WHERE title LIKE :query AND albumId = :albumId")
    fun getPhotosByAlbumId(query: String, albumId: Int): PagingSource<Int, PhotoEntity>

    @Query("Delete From photos")
    suspend fun clearAllPhotos()
}