package com.sektorpriz.obsidianmdcreator.feed.data.speechrecognizer

sealed interface SpeechData {

    data object ReadyForRecord : SpeechData

    data object StartRecord : SpeechData

    data object EndRecord : SpeechData

    data class RecordPart(val value: String) : SpeechData

    data class RecordResult(val value: String) : SpeechData
}