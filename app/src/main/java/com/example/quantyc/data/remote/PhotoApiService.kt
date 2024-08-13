package com.example.quantyc.data.remote

import com.example.quantyc.data.remote.response.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApiService {
    @GET("/photos")
    suspend fun getPhotos(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<PhotoResponse>
}