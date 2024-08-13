package com.example.quantyc.data.mapper

import com.example.quantyc.data.local.entities.PhotoEntity
import com.example.quantyc.data.remote.response.PhotoResponse
import com.example.quantyc.domain.model.Photo
import javax.inject.Inject

class PhotoMapper @Inject constructor() {
    fun mapFromEntity(entity: PhotoEntity): Photo = Photo(
        albumId = entity.albumId,
        id = entity.id,
        title = entity.title,
        url = entity.url,
        thumbnailUrl = entity.thumbnailUrl,
//        page = entity.page
    )

    fun mapToEntity(domainModel: Photo): PhotoEntity = PhotoEntity(
        albumId = domainModel.albumId,
        id = domainModel.id,
        title = domainModel.title,
        url = domainModel.url,
        thumbnailUrl = domainModel.thumbnailUrl,
//        page = domainModel.page
    )

    fun mapResponsesToEntity(responseModel: PhotoResponse): PhotoEntity = PhotoEntity(
        albumId = responseModel.albumId,
        id = responseModel.id,
        title = responseModel.title,
        url = responseModel.url,
        thumbnailUrl = responseModel.thumbnailUrl,
//        page = responseModel.page
    )
}

fun PhotoResponse.toPhotoEntity(page: Int): PhotoEntity {
    return PhotoEntity(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl,
//        page = page
    )
}
