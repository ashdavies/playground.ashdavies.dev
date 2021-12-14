package io.ashdavies.playground.events

import androidx.compose.ui.platform.LocalContext
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.StoreBuilder
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ashdavies.playground.DatabaseFactory
import io.ashdavies.playground.compose.staticComposableCompositionLocalOf
import io.ashdavies.playground.database.PlaygroundDatabase
import io.ashdavies.playground.database.PlaygroundDatabase.Companion.Schema
import io.ashdavies.playground.network.EventsService
import io.ashdavies.playground.network.LocalHttpClient

internal val LocalEventsStore = staticComposableCompositionLocalOf {
    val eventsService = EventsService(LocalHttpClient.current)
    val context = LocalContext.current

    val eventsQueries = DatabaseFactory { AndroidSqliteDriver(it, context) }
        .runBlocking { create(Schema) { PlaygroundDatabase(it) } }
        .eventsQueries

    StoreBuilder.from(
        sourceOfTruth = EventsSourceOfTruth(eventsQueries),
        fetcher = Fetcher.of { eventsService.get() },
    ).build()
}

private inline fun <T, R> T.runBlocking(crossinline block: suspend T.() -> R): R {
    return kotlinx.coroutines.runBlocking { block() }
}
