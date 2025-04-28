package io.ashdavies.tally.circuit

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.Pager
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.aggregator.PastConferencesCallable
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.identity.CredentialQueries
import io.ashdavies.identity.IdentityManager
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.events.AttendanceQueries
import io.ashdavies.tally.events.paging.PagedUpcomingEventsCallable
import io.ashdavies.tally.events.paging.rememberEventPager
import io.ashdavies.tally.gallery.File
import io.ashdavies.tally.gallery.GalleryPresenter
import io.ashdavies.tally.gallery.GalleryScreen
import io.ashdavies.tally.gallery.ImageManager
import io.ashdavies.tally.gallery.ImageQueries
import io.ashdavies.tally.gallery.PathProvider
import io.ashdavies.tally.gallery.StorageManager
import io.ashdavies.tally.gallery.SyncManager
import io.ashdavies.tally.gallery.inMemoryHttpClientEngine
import io.ashdavies.tally.gallery.readChannel
import io.ashdavies.tally.home.HomePresenter
import io.ashdavies.tally.home.HomeScreen
import io.ashdavies.tally.past.PastEventsPresenter
import io.ashdavies.tally.past.PastEventsScreen
import io.ashdavies.tally.upcoming.UpcomingEventsPresenter
import io.ashdavies.tally.upcoming.UpcomingEventsScreen
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import io.ashdavies.tally.events.Event as DatabaseEvent

@Composable
public fun rememberCircuit(
    playgroundDatabase: PlaygroundDatabase,
    platformContext: PlatformContext,
    httpClient: HttpClient,
    windowSizeClass: WindowSizeClass,
): Circuit {
    val eventPager = rememberEventPager(
        eventsCallable = PagedUpcomingEventsCallable(
            httpClient = httpClient,
            remoteConfig = remember { RemoteConfig() },
        ),
        eventsQueries = playgroundDatabase.eventsQueries,
    )

    return remember(platformContext) {
        val remoteAnalytics = RemoteAnalytics()
        val storageManager = StorageManager(
            platformContext = platformContext,
            pathProvider = PathProvider(platformContext),
            coroutineContext = Dispatchers.IO,
        )

        Circuit.Builder()
            .addHomeScreenCircuit(platformContext, playgroundDatabase.credentialQueries)
            .addUpcomingEventsScreenCircuit(eventPager, remoteAnalytics, windowSizeClass)
            .addGalleryScreenCircuit(
                storageManager,
                playgroundDatabase.imageQueries,
                remoteAnalytics,
            )
            .addPastEventsScreenCircuit(
                httpClient,
                playgroundDatabase.attendanceQueries,
                windowSizeClass,
            )
            .build()
    }
}

private fun Circuit.Builder.addHomeScreenCircuit(
    platformContext: PlatformContext,
    credentialQueries: CredentialQueries,
): Circuit.Builder = addCircuit<HomeScreen, HomeScreen.State>(
    presenterFactory = { _, navigator, _ ->
        presenterOf {
            HomePresenter(
                remoteConfig = RemoteConfig(),
                identityManager = IdentityManager(
                    platformContext = platformContext,
                    credentialQueries = credentialQueries,
                ),
                navigator = navigator,
            )
        }
    },
    uiFactory = { state, modifier ->
        HomeScreen(
            state = state,
            modifier = modifier,
            reportFullyDrawn = platformContext::reportFullyDrawn,
        )
    },
)

private fun Circuit.Builder.addUpcomingEventsScreenCircuit(
    eventPager: Pager<String, DatabaseEvent>,
    remoteAnalytics: RemoteAnalytics,
    windowSizeClass: WindowSizeClass,
): Circuit.Builder = addCircuit<UpcomingEventsScreen, UpcomingEventsScreen.State>(
    presenterFactory = { _, _, _ ->
        presenterOf {
            UpcomingEventsPresenter(
                eventPager = eventPager,
                remoteAnalytics = remoteAnalytics,
            )
        }
    },
    uiFactory = { state, modifier ->
        UpcomingEventsScreen(
            state = state,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    },
)

private fun Circuit.Builder.addGalleryScreenCircuit(
    storageManager: StorageManager,
    imageQueries: ImageQueries,
    remoteAnalytics: RemoteAnalytics,
): Circuit.Builder = addCircuit<GalleryScreen, GalleryScreen.State>(
    presenterFactory = { _, _, _ ->
        presenterOf {
            GalleryPresenter(
                imageManager = ImageManager(
                    storageManager = storageManager,
                    imageQueries = imageQueries,
                ),
                syncManager = SyncManager(
                    client = HttpClient(
                        engine = inMemoryHttpClientEngine(),
                        block = DefaultHttpConfiguration,
                    ),
                    reader = File::readChannel,
                ),
                remoteAnalytics = remoteAnalytics,
            )
        }
    },
    uiFactory = { state, modifier ->
        GalleryScreen(
            state = state,
            manager = storageManager,
            modifier = modifier,
        )
    },
)

private fun Circuit.Builder.addPastEventsScreenCircuit(
    httpClient: HttpClient,
    attendanceQueries: AttendanceQueries,
    windowSizeClass: WindowSizeClass,
): Circuit.Builder = addCircuit<PastEventsScreen, PastEventsScreen.State>(
    presenterFactory = { _, _, _ ->
        presenterOf {
            PastEventsPresenter(
                pastConferencesCallable = PastConferencesCallable(httpClient),
                attendanceQueries = attendanceQueries,
                ioDispatcher = Dispatchers.IO,
            )
        }
    },
    uiFactory = { state, modifier ->
        PastEventsScreen(
            state = state,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    },
)

private inline fun <reified S : Screen, UiState : CircuitUiState> Circuit.Builder.addCircuit(
    crossinline presenterFactory: (screen: S, navigator: Navigator, context: CircuitContext) -> Presenter<UiState>,
    crossinline uiFactory: @Composable (state: UiState, modifier: Modifier) -> Unit,
): Circuit.Builder = this
    .addPresenter(presenterFactory)
    .addUi<S, _>(uiFactory)
