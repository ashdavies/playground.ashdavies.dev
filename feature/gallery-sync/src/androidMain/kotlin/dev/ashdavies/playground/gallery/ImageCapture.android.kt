package dev.ashdavies.playground.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import kotlinx.io.files.Path
import java.io.File

@Composable
internal actual fun ImageCapture(onResult: (Path?) -> Unit, modifier: Modifier) {
    val target = remember { Path("image_capture.jpg") }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { onResult(if (it) target else null) },
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val authority = "${context.packageName}.files"
        val target = FileProvider.getUriForFile(context, authority, File(target.toString()))

        singlePhotoPickerLauncher.launch(target)
    }
}
