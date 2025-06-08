package com.sektorpriz.obsidianmdcreator.feed.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sektorpriz.obsidianmdcreator.feed.data.speechrecognizer.SpeechData
import com.sektorpriz.obsidianmdcreator.feed.data.speechrecognizer.SpeechRecognizerWrapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val speechRecognizer: SpeechRecognizerWrapper) : ViewModel() {

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
                audioPermissionState = PermissionState.NOT_REQUESTED,
                recordActive = false,
                recordPart = null,
                recordResult = null,
            )
        }
    }

    fun tryRecord() {
        val currentContent = (_state.value as? FeedState.FeedContent) ?: return

        if (currentContent.recordActive) {
            _state.value = currentContent.copy(recordActive = false)
            speechRecognizer.stop()
        } else {
            when (currentContent.audioPermissionState) {
                PermissionState.NOT_REQUESTED -> _state.value = currentContent.copy(audioPermissionState = PermissionState.REQUESTED)
                PermissionState.REQUESTED -> Unit
                PermissionState.GRANTED -> recordAudio()
                PermissionState.NOT_GRANTED -> Unit // Как-то это обработать, перевести пользователя в настройки
            }
        }
    }

    fun handlePermissionAnswer(permissionGranted: Boolean) {
        val currentContent = (_state.value as? FeedState.FeedContent) ?: return

        if (permissionGranted) {
            _state.value = currentContent.copy(audioPermissionState = PermissionState.GRANTED)
            recordAudio()
        } else {
            _state.value = currentContent.copy(audioPermissionState = PermissionState.NOT_GRANTED)
        }
    }

    private fun recordAudio() {
        val currentContent = (_state.value as? FeedState.FeedContent) ?: return
        _state.value = currentContent.copy(recordActive = true)

        collectAudio()
    }

    private fun collectAudio() {
        viewModelScope.launch {
            speechRecognizer.start().collect { data ->
                val currentContent = (_state.value as? FeedState.FeedContent) ?: throw CancellationException()

                when (data) {
                    SpeechData.EndRecord -> {
                        _state.value = currentContent.copy(recordActive = false)
                        Log.i("SpeechRecognizerVM", "EndRecord")
                    }
                    SpeechData.ReadyForRecord -> Log.i("SpeechRecognizerVM", "ReadyForRecord")
                    is SpeechData.RecordPart -> {
                        _state.value = currentContent.copy(recordPart = data.value)
                    }

                    is SpeechData.RecordResult -> {
                        _state.value = currentContent.copy(recordResult = data.value)
                    }

                    SpeechData.StartRecord -> Log.i("SpeechRecognizerVM", "StartRecord")
                }
            }
        }

    }
}