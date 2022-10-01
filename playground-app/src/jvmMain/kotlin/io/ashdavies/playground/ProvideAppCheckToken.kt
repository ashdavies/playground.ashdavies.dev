package io.ashdavies.playground

import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient

@Composable
public actual fun ProvideAppCheckToken(client: HttpClient, content: @Composable () -> Unit) {
    TODO("AppCheckToken not currently supported for JVM")
}
