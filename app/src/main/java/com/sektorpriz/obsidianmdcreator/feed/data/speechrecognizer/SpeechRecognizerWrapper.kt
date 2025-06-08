package com.sektorpriz.obsidianmdcreator.feed.data.speechrecognizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.sektorpriz.obsidianmdcreator.util.ktx.getActivity
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

class SpeechRecognizerWrapper(context: Context) {

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault().language)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    init {
        (context.getActivity() as? LifecycleOwner)?.lifecycle?.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    recognizer.destroy()
                }
            }
        )
    }

    fun start(): Flow<SpeechData> =
        callbackFlow {
            val listener = getRecognitionListener()

            recognizer.setRecognitionListener(listener)
            recognizer.startListening(intent)

            awaitClose {
                recognizer.stopListening()
            }
        }

    private fun ProducerScope<SpeechData>.getRecognitionListener(): RecognitionListener =
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                trySend(SpeechData.ReadyForRecord)
            }

            override fun onBeginningOfSpeech() {
                trySend(SpeechData.StartRecord)
            }

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() {
                trySend(SpeechData.EndRecord)
            }

            override fun onError(error: Int) {
                Log.e("SpeechRecorder", "Error $error")
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.i("SpeechRecorder", "Results ${matches?.joinToString()}")
                trySend(SpeechData.RecordResult(matches?.firstOrNull() ?: ""))
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.i("SpeechRecorder", "Results ${partial?.joinToString()}")
                trySend(SpeechData.RecordPart(partial?.firstOrNull() ?: ""))
            }

            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        }

    fun stop() {
        recognizer.stopListening()
    }
}