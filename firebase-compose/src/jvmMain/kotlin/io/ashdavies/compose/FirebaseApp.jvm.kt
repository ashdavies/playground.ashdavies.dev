package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.FirebasePlatform
import io.ashdavies.content.PlatformContext

@Composable
public actual fun FirebaseApp(content: @Composable () -> Unit) {
    LaunchedEffect(Unit) {
        FirebasePlatform.initializeFirebasePlatform(InMemoryFirebasePlatform())
    }

    CompositionLocalProvider(
        LocalFirebaseAndroidApp provides rememberFirebaseApp(PlatformContext.Default),
        content = content,
    )
}

private class InMemoryFirebasePlatform(private var storage: Map<String, String> = emptyMap()) : FirebasePlatform() {
    override fun store(key: String, value: String) = run { storage += key to value }
    override fun retrieve(key: String): String? = storage[key]
    override fun clear(key: String) = run { storage -= key }
    override fun log(msg: String) = println(msg)
}
