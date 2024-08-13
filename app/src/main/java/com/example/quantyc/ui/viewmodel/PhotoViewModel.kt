package com.example.quantyc.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.quantyc.domain.model.Photo
import com.example.quantyc.domain.usecase.GetPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val getPopularPhotosUseCase: GetPhotoUseCase
) : ViewModel() {
    var query by mutableStateOf("")
    var selectedAlbumId by mutableStateOf<Int?>(null)
    private val _photosFlow = MutableStateFlow(PagingData.empty<Photo>())
    val photosFlow: StateFlow<PagingData<Photo>> get() = _photosFlow

    fun getPhotos() {
        viewModelScope.launch {
            getPopularPhotosUseCase.invoke(query, selectedAlbumId)
                .cachedIn(viewModelScope)
                .collect {
                    _photosFlow.value = it
                }
        }
    }

    fun deleteAllCachedData() {
        viewModelScope.launch {
            getPopularPhotosUseCase.deleteAll()
        }
    }
}
