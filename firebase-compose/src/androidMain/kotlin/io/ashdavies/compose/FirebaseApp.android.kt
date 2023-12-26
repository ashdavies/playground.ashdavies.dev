package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
public actual fun FirebaseApp(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalFirebaseAndroidApp provides rememberFirebaseApp(LocalContext.current),
        content = content,
    )
}
