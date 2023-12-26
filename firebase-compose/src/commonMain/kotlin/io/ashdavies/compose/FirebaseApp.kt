package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.android.gms.common.util.ProcessUtils
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.app
import dev.gitlive.firebase.apps
import dev.gitlive.firebase.initialize
import io.ashdavies.content.PlatformContext

public val LocalFirebaseAndroidApp: ProvidableCompositionLocal<FirebaseApp> = staticCompositionLocalOf {
    error("CompositionLocal LocalFirebaseAndroidApp not present")
}

@Composable
public expect fun FirebaseApp(content: @Composable () -> Unit)

@Composable
internal fun rememberFirebaseApp(context: PlatformContext): FirebaseApp = remember(context) {
    val firebaseApp = when (Firebase.apps(context).isEmpty()) {
        true -> Firebase.initialize(context)
        else -> Firebase.app
    }

    requireNotNull(firebaseApp) {
        "Default FirebaseApp is not initialized in this " +
            "process ${ProcessUtils.getMyProcessName()}. Make sure " +
            "to call FirebaseApp.initializeApp(Context) first."
    }
}
