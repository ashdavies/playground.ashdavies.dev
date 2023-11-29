package io.ashdavies.gallery

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
    onCapture: (File) -> Unit,
) {
    val platformContext = LocalContext.current
    val fileState by produceState<File?>(null) {
        value = manager.create(platformContext)
    }

    val activityResultContract = ActivityResultContracts.TakePicture()
    val file = requireNotNull(fileState) { "File must not be null" }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(activityResultContract) {
        onCapture(file)
    }

    LaunchedEffect(Unit) {
        val authority = "${platformContext.packageName}.files"
        val target = FileProvider.getUriForFile(
            /* context = */
            platformContext,
            /* authority = */
            authority,
            /* file = */
            file,
        )

        singlePhotoPickerLauncher.launch(target)
    }
}
