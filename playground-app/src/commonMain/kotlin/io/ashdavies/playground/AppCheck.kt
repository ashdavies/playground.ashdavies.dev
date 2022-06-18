package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
public expect fun AppCheckListener(listener: (String) -> Unit)

@Composable
public fun ProvideAppCheckToken(content: @Composable () -> Unit) {
    var _token: String? by remember { mutableStateOf(null) }
    val token: String? = _token

    AppCheckListener { _token = it }

    if (token != null) {
        CompositionLocalProvider(LocalAppCheckToken provides token) {
            content()
        }
    }
}
