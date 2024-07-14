package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

private val ProvidableLocalHttpClient: ProvidableCompositionLocal<HttpClient> =
    staticCompositionLocalOf { error("CompositionLocal LocalHttpClient not present") }

public val LocalHttpClient: CompositionLocal<HttpClient>
    get() = ProvidableLocalHttpClient

@RequiresOptIn
public annotation class HttpClientProvider

@Composable
@OptIn(HttpClientProvider::class)
public fun ProvideHttpClient(
    configure: HttpClientConfig<*>.() -> Unit = { },
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        ProvidableLocalHttpClient provides remember { httpClient(block = configure) },
        content = content,
    )
}

@HttpClientProvider
public fun httpClient(
    engine: HttpClientEngine = OkHttp.create { },
    block: HttpClientConfig<*>.() -> Unit = { },
): HttpClient = HttpClient(engine) {
    install(ContentNegotiation) {
        json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    block()
}

public fun Configuration.json(
    from: Json = Json.Default,
    builderAction: JsonBuilder.() -> Unit,
) {
    json(Json(from, builderAction))
}
