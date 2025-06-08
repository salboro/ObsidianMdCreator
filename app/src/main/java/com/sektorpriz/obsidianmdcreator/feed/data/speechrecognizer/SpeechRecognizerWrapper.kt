package com.sektorpriz.obsidianmdcreator.feed.data.speechrecognizer

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.sektorpriz.obsidianmdcreator.util.ktx.getActivity

class SpeechRecognizerWrapper(private val context: Context) {

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    init {
        recognizer.setRecognitionListener(
            object : RecognitionListener {
                override fun onReadyForSpeech(p0: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onBeginningOfSpeech() {
                    TODO("Not yet implemented")
                }

                override fun onRmsChanged(p0: Float) {
                    TODO("Not yet implemented")
                }

                override fun onBufferReceived(p0: ByteArray?) {
                    TODO("Not yet implemented")
                }

                override fun onEndOfSpeech() {
                    TODO("Not yet implemented")
                }

                override fun onError(p0: Int) {
                    TODO("Not yet implemented")
                }

                override fun onResults(p0: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onPartialResults(p0: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onEvent(p0: Int, p1: Bundle?) {
                    TODO("Not yet implemented")
                }

            }
        )

        (context.getActivity() as? LifecycleOwner)?.lifecycle?.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    recognizer.destroy()
                }
            }
        )
    }
}