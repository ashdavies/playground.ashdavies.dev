package io.ashdavies.notion

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual object HttpClientFactory {
    actual val engine: HttpClientEngineFactory<*> = OkHttp
}
