package io.ashdavies.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.util.ProcessUtils
import com.google.firebase.FirebaseApp

public val LocalFirebaseApp: ProvidableCompositionLocal<FirebaseApp> = staticCompositionLocalOf {
    noLocalProvidedFor("LocalFirebaseApp")
}

@Composable
public fun ProvideFirebaseApp(context: Context = LocalContext.current, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalFirebaseApp provides requireFirebaseApp(context),
        content = content,
    )
}

private fun requireFirebaseApp(context: Context): FirebaseApp {
    val firebaseApp = if (FirebaseApp.getApps(context).size > 0) FirebaseApp.getInstance()
    else FirebaseApp.initializeApp(context)

    return requireNotNull(firebaseApp) {
        "Default FirebaseApp is not initialized in this process ${ProcessUtils.getMyProcessName()}. " +
                "Make sure to call FirebaseApp.initializeApp(Context) first."
    }
}
