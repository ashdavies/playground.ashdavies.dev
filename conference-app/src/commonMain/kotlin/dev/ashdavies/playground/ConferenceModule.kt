package dev.ashdavies.playground

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.gallery.imageAdapter
import dev.ashdavies.playground.http.FirebaseAppCheck
import dev.ashdavies.sql.DatabaseFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import dev.ashdavies.playground.PlaygroundDatabase as ConferenceAppDatabase
import dev.ashdavies.playground.event.common.PlaygroundDatabase as EventCommonDatabase
import dev.ashdavies.playground.gallery.PlaygroundDatabase as GalleryDatabase

@ContributesTo(AppScope::class)
internal interface ConferenceModule {

    @Provides
    fun conferenceAppDatabaseFactory(context: PlatformContext): DatabaseFactory<ConferenceAppDatabase> {
        return DatabaseFactory(ConferenceAppDatabase.Schema, context, ConferenceAppDatabase::invoke)
    }

    @Provides
    fun eventCommonDatabaseFactory(context: PlatformContext): DatabaseFactory<EventCommonDatabase> {
        return DatabaseFactory(EventCommonDatabase.Schema, context, EventCommonDatabase::invoke)
    }

    @Provides
    fun galleryDatabaseFactory(context: PlatformContext): DatabaseFactory<GalleryDatabase> {
        return DatabaseFactory(GalleryDatabase.Schema, context) { GalleryDatabase(it, imageAdapter()) }
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
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()
}
