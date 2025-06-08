package com.sektorpriz.obsidianmdcreator.feed.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sektorpriz.obsidianmdcreator.feed.data.speechrecognizer.SpeechRecognizerWrapper

class FeedViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val recognizer = SpeechRecognizerWrapper(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(recognizer) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class $modelClass")
    }
}