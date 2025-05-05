package io.ashdavies.tally

import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.sql.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header

internal object JvmTallyModule {

    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header("User-Agent", System.getProperty("os.name"))
            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
        }

        install(HttpCache) {
            publicStorage(context.resolveCacheDir())
        }
    }

    fun playgroundDatabase(context: PlatformContext) = DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it) },
    )
}
