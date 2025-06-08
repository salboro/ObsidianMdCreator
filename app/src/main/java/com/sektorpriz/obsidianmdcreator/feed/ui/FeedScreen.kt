package com.sektorpriz.obsidianmdcreator.feed.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sektorpriz.obsidianmdcreator.feed.presentation.FeedState
import com.sektorpriz.obsidianmdcreator.feed.presentation.FeedViewModel
import com.sektorpriz.obsidianmdcreator.feed.presentation.FeedViewModelFactory

@Composable
fun FeedScreen() {
    val context = LocalContext.current
    val viewModel: FeedViewModel = viewModel(factory = FeedViewModelFactory(context))
    val state = viewModel.state.collectAsStateWithLifecycle().value

    when (state) {
        FeedState.Loading        -> LoadingState()
        is FeedState.FeedContent -> FeedContent(state, viewModel::tryRecord) { granted ->
            val text = if (granted) {
                "Granted"
            } else {
                "Not Granted"
            }
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

            viewModel.handlePermissionAnswer(granted)
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoadingState()
}