package com.sektorpriz.obsidianmdcreator.feed.ui

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sektorpriz.obsidianmdcreator.R
import com.sektorpriz.obsidianmdcreator.feed.presentation.FeedState
import com.sektorpriz.obsidianmdcreator.feed.presentation.Note
import com.sektorpriz.obsidianmdcreator.feed.presentation.PermissionState
import com.sektorpriz.obsidianmdcreator.util.ui.RequestPermission

@Composable
fun FeedContent(
    state: FeedState.FeedContent,
    onRecordClick: () -> Unit,
    onAudioPermissionRequested: (granted: Boolean) -> Unit,
) {
    Scaffold(topBar = { AppBar() }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(state.notes) {
                    NoteItem(it)
                }
            }

            RecordButton(onRecordClick)
        }

        if (state.audioPermissionState == PermissionState.REQUESTED) {
            val context = LocalContext.current
            RequestPermission(
                permission = Manifest.permission.RECORD_AUDIO,
                onPermissionRequested = onAudioPermissionRequested,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(title = {
        Text("ObsidianMdCreator")
    })
}

@Composable
private fun NoteItem(note: Note) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = note.title,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = note.body,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RecordButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .size(144.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        FilledIconButton(
            onClick = onClick,
        ) {
            Icon(
                modifier = Modifier.size(144.dp),
                painter = painterResource(R.drawable.mic_big),
                contentDescription = "Record"
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    val state = FeedState.FeedContent(
        listOf(
            Note(title = "title", body = "Yasos Biba", tags = emptyList()),
            Note(title = "title2", body = "Bombordilo Crocodilo", tags = emptyList()),
            Note(title = "title3", body = "Capuccino Asssassino", tags = emptyList()),
        ),
        audioPermissionState = PermissionState.NOT_GRANTED
    )

    FeedContent(state, {}, {})
}