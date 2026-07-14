package dev.ashdavies.playground

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.http.createAppCheckAuthProvider
import dev.ashdavies.sql.DatabaseFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.request.header
import dev.ashdavies.playground.PlaygroundDatabase as ConferenceAppDatabase
import dev.ashdavies.playground.event.common.PlaygroundDatabase as EventCommonDatabase

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
    fun httpClient(@Named("httpClientHeaders") headers: Set<Pair<String, String>>): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            headers.forEach { (key, value) -> header(key, value) }
            header("X-API-Key", BuildConfig.API_KEY)
        }

        install(Auth) {
            val appId = requireNotNull(BuildConfig.APP_ID) { "APP_ID was null" }
            providers.add(createAppCheckAuthProvider(appId))
        }
    }

    @Provides
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()
}
