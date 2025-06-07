package com.sektorpriz.obsidianmdcreator.feed.presentation

sealed interface FeedState {

    data object Loading : FeedState

    data class FeedContent(
        val notes: List<Note>,
    ): FeedState
}