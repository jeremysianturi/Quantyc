package com.example.quantyc.domain.repository

import androidx.paging.PagingData
import com.example.quantyc.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos(query: String, selectedAlbumId: Int?): Flow<PagingData<Photo>>
    suspend fun deleteAll()
}