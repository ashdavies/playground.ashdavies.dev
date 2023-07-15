package io.ashdavies.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

@Composable
internal actual fun CameraOverlay(
    paths: io.ashdavies.gallery.FileProvider,
    modifier: Modifier,
    onCapture: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val target = remember(context) {
        val file = createNewFile(File(paths.images), "${UUID.randomUUID()}.jpg")
        val authority = "${context.packageName}.files"

        FileProvider.getUriForFile(context, authority, file)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { onCapture(Uri.create("$target")) },
    )

    LaunchedEffect(Unit) {
        singlePhotoPickerLauncher.launch(target)
    }
}

private fun createNewFile(parent: File, child: String): File {
    return File(parent, child).apply { createNewFile() }
}
