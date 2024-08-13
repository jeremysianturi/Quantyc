package com.example.quantyc.domain.usecase

import androidx.paging.PagingData
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.domain.model.Photo
import com.example.quantyc.domain.repository.AuthRepository
import com.example.quantyc.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun loginUser(email: String, password: String): Flow<Boolean> = repository.loginUser(email,password)
    suspend fun registerUser(userEntity: UserEntity): Boolean = repository.registerUser(userEntity)
    suspend fun fetchUser(userEmail: String): UserEntity = repository.fetchUser(userEmail)
    suspend fun getUsers(): Flow<List<UserEntity>> = repository.getUsers()
    suspend fun deleteUser(email: String): Boolean = repository.deleteUser(email)
    suspend fun updateUser(userEntity: UserEntity): Boolean = repository.updateUser(userEntity)

}