@file:JvmName("AppCheckTokenJvm")

package io.ashdavies.check

import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient

@Composable
public actual fun ProvideAppCheckToken(client: HttpClient, content: @Composable () -> Unit) {
    content()
}
