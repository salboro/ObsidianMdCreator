package com.sektorpriz.obsidianmdcreator.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {

    private val _state = MutableStateFlow<FeedState>(FeedState.Loading)
    val state: StateFlow<FeedState> = _state

    init {
        loadingData()
    }

    private fun loadingData() {
        viewModelScope.launch {
            delay(1000L)
            _state.value = FeedState.FeedContent(
                notes = listOf(
                    Note(title = "title", body = "Yasos Biba", tags = emptyList()),
                    Note(title = "title2", body = "Bombordilo Crocodilo", tags = emptyList()),
                    Note(title = "title3", body = "Capuccino Asssassino", tags = emptyList()),
                ),
                audioPermissionState = PermissionState.NOT_REQUESTED
            )
        }
    }

    fun tryRecord() {
        val currentContent = (_state.value as? FeedState.FeedContent) ?: return
        _state.value = currentContent.copy(audioPermissionState = PermissionState.REQUESTED)
    }

    fun recordAudio(permissionGranted: Boolean) {
        val currentContent = (_state.value as? FeedState.FeedContent) ?: return
        val permissionState = if (permissionGranted) {
            PermissionState.GRANTED
        } else {
            PermissionState.NOT_GRANTED
        }
        _state.value = currentContent.copy(audioPermissionState = permissionState)
    }
}