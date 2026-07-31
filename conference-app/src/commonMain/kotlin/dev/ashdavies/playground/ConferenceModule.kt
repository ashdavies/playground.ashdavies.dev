package dev.ashdavies.playground

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.getBoolean
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.common.models.XApiKey
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.gallery.LocalGallery
import dev.ashdavies.playground.gallery.imageAdapter
import dev.ashdavies.playground.http.FirebaseAppCheck
import dev.ashdavies.sql.DriverFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import dev.ashdavies.playground.event.common.PlaygroundDatabase as CommonDatabase
import dev.ashdavies.playground.gallery.PlaygroundDatabase as GalleryDatabase

@ContributesTo(AppScope::class)
internal interface ConferenceModule {

    @Binds
    @ExperimentalMetroCoroutinesApi
    fun commonDatabase(playgroundDatabase: PlaygroundDatabase): CommonDatabase

    @Binds
    @ExperimentalMetroCoroutinesApi
    fun galleryDatabase(playgroundDatabase: PlaygroundDatabase): GalleryDatabase

    @Provides
    @ExperimentalMetroCoroutinesApi
    suspend fun playgroundDatabase(context: PlatformContext): PlaygroundDatabase = PlaygroundDatabase(
        driver = DriverFactory(PlaygroundDatabase.Schema, context, "database.db"),
        imageAdapter = imageAdapter(),
    )

    @Provides
    fun httpClient(@Named("httpClientHeaders") headers: Set<Pair<String, String>>): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header(HttpHeaders.XApiKey, requireNotNull(BuildConfig.API_KEY) { "API_KEY was null" })
            headers.forEach { (key, value) -> header(key, value) }

            url(BuildConfig.PLAYGROUND_BASE_URL ?: "https://api.ashdavies.dev/")
        }

        install(FirebaseAppCheck)
    }

    @Provides
    @ExperimentalMetroCoroutinesApi
    suspend fun localGallery(remoteConfig: RemoteConfig): LocalGallery = LocalGallery(
        enabled = remoteConfig.getBoolean("local_gallery"),
    )

    @Provides
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()
}
