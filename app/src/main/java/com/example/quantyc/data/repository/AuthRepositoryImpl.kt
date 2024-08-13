package com.example.quantyc.data.repository

import android.util.Log
import androidx.paging.PagingData
import com.example.quantyc.data.local.PhotoDatabase
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val photosDatabase: PhotoDatabase
) : AuthRepository  {

    override suspend fun registerUser(userEntity: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val existingUser = photosDatabase.getUserDao().checkIfUserExists(userEntity.email)
            if (existingUser == null) {
                photosDatabase.getUserDao().insertUser(userEntity)
                true
            } else {
                false
            }
        }
    }

    override suspend fun loginUser(email: String, password: String): Flow<Boolean> {
        return photosDatabase.getUserDao().loginUser(email, password).map { userEntity ->
            userEntity != null
        }
    }

    override suspend fun fetchUser(userEmail: String): UserEntity {
        return photosDatabase.getUserDao().fetchUser(userEmail)
    }

    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return photosDatabase.getUserDao().getUsers()
    }

    override suspend fun deleteUser(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val existingUser = photosDatabase.getUserDao().checkIfUserExists(email)
            if (existingUser != null) {
                val success = photosDatabase.getUserDao().deleteUser(email)
                Log.d("AuthRepositoryImpl", "check value => $success")
                if (success == 1){
                    true
                } else {
                    false
                }

            } else {
                false
            }
        }
    }

    override suspend fun updateUser(userEntity: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            photosDatabase.getUserDao().update(userEntity.username, userEntity.email, userEntity.id) != -1
        }
    }
}