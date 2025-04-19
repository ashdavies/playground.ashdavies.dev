package io.ashdavies.tally.metro

import androidx.compose.runtime.Composable
import androidx.paging.Pager
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.aggregator.callable.PastConferencesCallable
import io.ashdavies.identity.IdentityManager
import io.ashdavies.tally.compose.DrawnReporter
import io.ashdavies.tally.events.AttendanceQueries
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.gallery.ImageManager
import io.ashdavies.tally.gallery.StorageManager
import io.ashdavies.tally.gallery.SyncManager
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.sql.LocalTransacter
import kotlinx.coroutines.CoroutineDispatcher

internal fun interface ComposeProvider<T> {

    @Composable
    operator fun invoke(): T
}

internal typealias EventPager = Pager<String, Event>

@DependencyGraph
internal interface TallyAppGraph {
    val attendanceQueriesProvider: ComposeProvider<AttendanceQueries>
    val pastPreferencesCallable: PastConferencesCallable
    val ioDispatcher: CoroutineDispatcher
    val identityManager: IdentityManager
    val storageManager: StorageManager
    val drawnReporter: DrawnReporter
    val imageManager: ImageManager
    val syncManager: SyncManager
    val eventPager: EventPager
}

internal interface TallyAppModule {

    @Provides
    private fun attendanceQueriesProvider(playgroundDatabase: PlaygroundDatabase): ComposeProvider<AttendanceQueries> {
        return ComposeProvider { (LocalTransacter.current as PlaygroundDatabase).attendanceQueries }
    }
}
