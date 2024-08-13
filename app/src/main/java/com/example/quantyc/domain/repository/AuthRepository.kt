package com.example.quantyc.domain.repository

import androidx.paging.PagingData
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Flow<Boolean>
    suspend fun registerUser(userEntity: UserEntity): Boolean
    suspend fun fetchUser(userEmail: String): UserEntity
    suspend fun getUsers(): Flow<List<UserEntity>>
    suspend fun deleteUser(email: String): Boolean
    suspend fun updateUser(userEntity: UserEntity): Boolean
}