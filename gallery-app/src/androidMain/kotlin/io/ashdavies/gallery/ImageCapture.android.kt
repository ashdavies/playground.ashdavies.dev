package io.ashdavies.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider

@Composable
internal actual fun ImageCapture(
    manager: StorageManager,
    modifier: Modifier,
    onCapture: (File) -> Unit,
) {
    val context = LocalContext.current
    val file = remember(context) {
        manager.create(context)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { onCapture(file) },
    )

    LaunchedEffect(Unit) {
        val authority = "${context.packageName}.files"
        val target = FileProvider.getUriForFile(
            /* context = */ context,
            /* authority = */ authority,
            /* file = */ file,
        )

        singlePhotoPickerLauncher.launch(target)
    }
}
