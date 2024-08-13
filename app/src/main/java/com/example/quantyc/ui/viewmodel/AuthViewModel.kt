package com.example.quantyc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.domain.model.Photo
import com.example.quantyc.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    var email = MutableStateFlow("")
    var password = MutableStateFlow("")
    var username = MutableStateFlow("")
    var role = MutableStateFlow("User")

    private val _loginState = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

    private val _loginErrorState = MutableStateFlow(false)
    val loginErrorState: StateFlow<Boolean> = _loginErrorState

    private val _registerState = MutableStateFlow(false)
    val registerState: StateFlow<Boolean> = _registerState

    private val _usersFlow = MutableStateFlow(emptyList<UserEntity>())
    val usersFlow: StateFlow<List<UserEntity>> get() = _usersFlow

    private val _deleteUserState = MutableStateFlow(false)
    val deleteUserState: StateFlow<Boolean> = _registerState

    private val _updateUserState = MutableStateFlow(true)
    val updateUserState: StateFlow<Boolean> = _updateUserState

    fun register() {
        viewModelScope.launch {
            val success = userUseCase.registerUser(
                UserEntity(
                    username = username.value,
                    email = email.value,
                    password = password.value,
                    role = role.value
                )
            )
            _registerState.value = success
        }
    }

    fun login() {
        viewModelScope.launch {
            userUseCase.loginUser(email.value, password.value).collectLatest { success ->
                if (success) {
                    _loginState.value = true
                    _loginErrorState.value = false
                } else {
                    _loginErrorState.value = true
                    _loginState.value = false
                }
            }
        }
    }

    fun dismissLoginErrorDialog() {
        _loginErrorState.value = false
    }

    fun getCurrentUser(): UserEntity {
        return UserEntity(
            username = username.value,
            email = email.value,
            password = password.value,
            role = role.value
        )
    }

    suspend fun fetchUser(userEmail: String): UserEntity{
        return userUseCase.fetchUser(userEmail)
    }

    fun getUsers() {
        viewModelScope.launch {
            userUseCase.getUsers()
                .collect {
                    _usersFlow.value = it
                }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            val success = userUseCase.deleteUser(email)
            _deleteUserState.value = success
        }
    }

    fun updateUser(userEntity: UserEntity){
        viewModelScope.launch {
            val success = userUseCase.updateUser(userEntity)
            _updateUserState.value = success
        }
    }

    fun resetUpdateUserState() {
        _updateUserState.value = true
    }
}