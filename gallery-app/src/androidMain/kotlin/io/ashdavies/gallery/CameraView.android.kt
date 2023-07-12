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

public actual typealias Uri = java.net.URI

@Composable
public actual fun CameraView(
    modifier: Modifier,
    onCapture: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val target = remember(context) {
        val authority = "${context.packageName}.files"
        val path = File(context.filesDir, "images")
        val file = File(path, "${UUID.randomUUID()}.jpg")

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
