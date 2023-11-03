package io.ashdavies.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.util.ProcessUtils
import com.google.firebase.FirebaseApp

public val LocalFirebaseAndroidApp: ProvidableCompositionLocal<FirebaseApp> = staticCompositionLocalOf {
    error("CompositionLocal LocalFirebaseAndroidApp not present")
}

@Composable
public fun FirebaseApp(context: Context = LocalContext.current, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalFirebaseAndroidApp provides requireFirebaseApp(context),
        content = content,
    )
}

private fun requireFirebaseApp(context: Context): FirebaseApp {
    val firebaseApp = when (FirebaseApp.getApps(context).isEmpty()) {
        true -> FirebaseApp.initializeApp(context)
        else -> FirebaseApp.getInstance()
    }

    return requireNotNull(firebaseApp) {
        "Default FirebaseApp is not initialized in this " +
            "process ${ProcessUtils.getMyProcessName()}. Make sure " +
            "to call FirebaseApp.initializeApp(Context) first."
    }
}
