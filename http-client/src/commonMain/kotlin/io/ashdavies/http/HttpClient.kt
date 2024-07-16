package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    HttpClient(DefaultHttpConfiguration)
}

public fun HttpClientConfig<*>.default() {
    DefaultHttpConfiguration()
}

public val DefaultHttpConfiguration: HttpClientConfig<*>.() -> Unit = {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
}

@Composable
public fun ProvideHttpClient(
    config: HttpClientConfig<*>.() -> Unit,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalHttpClient provides LocalHttpClient.current.config(config),
        content = content,
    )
}
