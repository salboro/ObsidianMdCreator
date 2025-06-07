package com.sektorpriz.obsidianmdcreator.feed.presentation

data class Note(
    val title: String,
    val body: String,
    val tags: List<String>,
)