package com.example.quantyc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quantyc.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun loginUser(email: String, password: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun checkIfUserExists(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun fetchUser(email: String): UserEntity

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUser(email: String) : Int

    @Query("UPDATE users SET username=:userName, email = :email WHERE id = :id")
    fun update(userName: String, email: String, id: Int): Int
}