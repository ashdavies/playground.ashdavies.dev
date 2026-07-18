package dev.ashdavies.playground

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.getBoolean
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.gallery.LocalGallery
import dev.ashdavies.playground.gallery.imageAdapter
import dev.ashdavies.playground.http.FirebaseAppCheck
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.Suspended
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import dev.ashdavies.playground.event.common.PlaygroundDatabase as CommonDatabase
import dev.ashdavies.playground.gallery.PlaygroundDatabase as GalleryDatabase

@ContributesTo(AppScope::class)
internal interface ConferenceModule {

    @Binds
    fun commonDatabaseFactory(playgroundDatabaseFactory: DatabaseFactory<PlaygroundDatabase>): DatabaseFactory<CommonDatabase>

    @Binds
    fun galleryDatabaseFactory(playgroundDatabaseFactory: DatabaseFactory<PlaygroundDatabase>): DatabaseFactory<GalleryDatabase>

    @Provides
    fun playgroundDatabaseFactory(context: PlatformContext): DatabaseFactory<PlaygroundDatabase> {
        return DatabaseFactory(PlaygroundDatabase.Schema, context) { PlaygroundDatabase(it, imageAdapter()) }
    }

    @Provides
    fun httpClient(@Named("httpClientHeaders") headers: Set<Pair<String, String>>): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            headers.forEach { (key, value) -> header(key, value) }
            header("X-API-Key", BuildConfig.API_KEY)
        }

        install(FirebaseAppCheck)
    }

    @Provides
    fun localGallery(remoteConfig: RemoteConfig): LocalGallery = LocalGallery(
        enabled = Suspended { remoteConfig.getBoolean("local_gallery") },
    )

    @Provides
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()
}
