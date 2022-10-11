package io.ashdavies.check

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import io.ashdavies.compose.ProvideFirebaseApp

public val FirebaseApp.appCheck: FirebaseAppCheck
    get() = FirebaseAppCheck.getInstance(this)

@Composable
public fun ProvideFirebaseApp(context: Context = LocalContext.current, content: @Composable () -> Unit) {
    ProvideFirebaseApp(context) { ProvideAppCheckToken(content) }
}
