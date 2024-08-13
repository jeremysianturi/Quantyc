package com.example.quantyc.data.remote.response

import com.example.quantyc.data.local.entities.PhotoEntity
import com.google.gson.annotations.SerializedName

data class PhotoResponse(

    @SerializedName(value = "albumId")
    val albumId: Int,

    @SerializedName(value = "id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String
)