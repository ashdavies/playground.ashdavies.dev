package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.application
import com.google.cloud.functions.HttpFunction

public fun HttpApplication(block: @Composable () -> Unit): HttpFunction = HttpFunction { request, response ->
    application {
        CompositionLocalProvider(
            LocalHttpRequest provides request,
            LocalHttpResponse provides response,
        ) { block() }
    }
}
