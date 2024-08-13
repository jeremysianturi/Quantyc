package com.example.quantyc.domain.usecase

import androidx.paging.PagingData
import com.example.quantyc.domain.model.Photo
import com.example.quantyc.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    fun invoke(query: String, selectedAlbumId: Int?): Flow<PagingData<Photo>> = repository.getPhotos(query,selectedAlbumId)
    suspend fun deleteAll() = repository.deleteAll()
}