package com.sektorpriz.obsidianmdcreator.feed.presentation

sealed interface FeedState {

    data object Loading : FeedState

    data class FeedContent(
        val notes: List<Note>,
        val audioPermissionState: PermissionState,
    ): FeedState
}

enum class PermissionState {
    NOT_REQUESTED,
    REQUESTED,
    GRANTED,
    NOT_GRANTED,
}