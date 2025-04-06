package io.ashdavies.tally.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider

@Composable
internal actual fun ImageCapture(
    manager: StorageManager,
    modifier: Modifier,
    onCapture: (File?) -> Unit,
) {
    val context = LocalContext.current
    val fileState by produceState<File?>(null) {
        value = manager.create()
    }

    fileState?.let { file ->
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { onCapture(if (it) file else null) },
        )

        LaunchedEffect(Unit) {
            val authority = "${context.packageName}.files"
            val target = FileProvider.getUriForFile(context, authority, file)

            singlePhotoPickerLauncher.launch(target)
        }
    }
}
