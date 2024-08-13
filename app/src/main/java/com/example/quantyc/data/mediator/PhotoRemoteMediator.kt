package com.example.quantyc.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.quantyc.data.local.PhotoDatabase
import com.example.quantyc.data.local.entities.PhotoEntity
import com.example.quantyc.data.local.entities.RemoteKeysEntity
import com.example.quantyc.data.mapper.PhotoMapper
import com.example.quantyc.data.remote.PhotoApiService
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val photosApiService: PhotoApiService,
    private val photosDatabase: PhotoDatabase,
    private val photoMapper: PhotoMapper
): RemoteMediator<Int, PhotoEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdateTime = photosDatabase.getRemoteKeysDao().getCreationTime() ?: 0

        return if (System.currentTimeMillis() - lastUpdateTime < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { photo ->
            photosDatabase.getRemoteKeysDao().getRemoteKeyByPhotoID(photo.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PhotoEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { photo ->
            photosDatabase.getRemoteKeysDao().getRemoteKeyByPhotoID(photo.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PhotoEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                photosDatabase.getRemoteKeysDao().getRemoteKeyByPhotoID(id)
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiResponse = photosApiService.getPhotos(page,10)
            val photos = apiResponse.map { photo ->
                photoMapper.mapResponsesToEntity(photo)
            }
            val endOfPaginationReached = photos.isEmpty()

            photosDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    photosDatabase.getRemoteKeysDao().clearRemoteKeys()
                    photosDatabase.getPhotoDao().clearAllPhotos()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = photos.map {
                    RemoteKeysEntity(photoID = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                }
                photosDatabase.getRemoteKeysDao().insertAll(remoteKeys)
                photosDatabase.getPhotoDao().insertAll(photos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}
