package com.example.quantyc.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.quantyc.data.local.PhotoDatabase
import com.example.quantyc.data.mapper.PhotoMapper
import com.example.quantyc.data.mediator.PhotoRemoteMediator
import com.example.quantyc.data.remote.PhotoApiService
import com.example.quantyc.domain.model.Photo
import com.example.quantyc.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PhotoRepositoryImpl @Inject constructor(
    private val photosApiService: PhotoApiService,
    private val photosDatabase: PhotoDatabase,
    private val photoMapper: PhotoMapper
) : PhotoRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotos(query: String, selectedAlbumId: Int?): Flow<PagingData<Photo>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10,
            initialLoadSize = 20,
        ),
        pagingSourceFactory = {
            if (selectedAlbumId != null) {
                photosDatabase.getPhotoDao().getPhotosByAlbumId("%${query}%", selectedAlbumId)
            } else {
                photosDatabase.getPhotoDao().getPhotos("%${query}%")
            }
        },
        remoteMediator = PhotoRemoteMediator(
            photosApiService,
            photosDatabase,
            photoMapper
        )
    ).flow.map { pagingData ->
        pagingData.map { photoEntity ->
            photoMapper.mapFromEntity(photoEntity)
        }
    }

    override suspend fun deleteAll() {
        photosDatabase.getPhotoDao().clearAllPhotos()
    }


}