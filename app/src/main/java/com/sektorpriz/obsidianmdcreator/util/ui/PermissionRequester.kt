package com.sektorpriz.obsidianmdcreator.util.ui

import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.sektorpriz.obsidianmdcreator.util.ktx.getActivity
import kotlinx.coroutines.launch

@Composable
fun RequestPermission(
    permission: String,
    onPermissionRequested: (granted: Boolean) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionRequested(isGranted)
    }

    when {
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED ->
            onPermissionRequested(true)

        context.getActivity().shouldShowRequestPermissionRationale(permission) ->
            PermissionBottomSheet(launcher, permission)

        else -> LaunchedEffect(Unit) { launcher.launch(permission) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionBottomSheet(
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    permission: String
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet = false
        },
        sheetState = sheetState
    ) {
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet = false
                }
                launcher.launch(permission)
            }
        }) {
            Text("Dai monkyiu")
        }
    }
}