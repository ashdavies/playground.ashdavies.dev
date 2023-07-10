package io.ashdavies.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

public actual typealias Uri = java.net.URI

@Composable
public actual fun CameraView(
    modifier: Modifier,
    onCapture: (Uri) -> Unit
) {
    val context = LocalContext.current
    val target = FileProvider.getUriForFile(
        /* context = */ context,
        /* authority = */ "${context.packageName}.files",
        /* file = */ File(
            /* parent = */ File(
                /* parent = */ context.cacheDir,
                /* child = */ "images",
            ),
            /* child = */ "capture.jpg",
        )
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { onCapture(Uri.create("$target")) },
    )

    LaunchedEffect(Unit) {
        singlePhotoPickerLauncher.launch(target)
    }
}
