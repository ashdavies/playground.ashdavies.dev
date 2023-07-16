package io.ashdavies.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun CameraOverlay(
    manager: StorageManager,
    modifier: Modifier,
    onCapture: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val target = remember(context) {
        manager.createFileUri(context)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { onCapture(target) },
    )

    LaunchedEffect(Unit) {
        singlePhotoPickerLauncher.launch(target)
    }
}
