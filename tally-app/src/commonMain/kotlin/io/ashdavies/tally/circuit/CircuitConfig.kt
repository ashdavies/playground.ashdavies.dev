package io.ashdavies.tally.circuit

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
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.identity.IdentityManager
import io.ashdavies.sql.LocalTransacter
import io.ashdavies.tally.events.paging.rememberEventPager
import io.ashdavies.tally.gallery.File
import io.ashdavies.tally.gallery.GalleryPresenter
import io.ashdavies.tally.gallery.GalleryScreen
import io.ashdavies.tally.gallery.ImageManager
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
import io.ashdavies.tally.PlaygroundDatabase
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import io.ashdavies.tally.events.Event as DatabaseEvent

@Composable
public fun rememberCircuit(
    platformContext: PlatformContext,
    eventPager: Pager<String, DatabaseEvent> = rememberEventPager(),
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
): Circuit = remember(platformContext) {
    val identityManager = IdentityManager(platformContext, playgroundDatabase.credentialQueries)
    val imageManager = ImageManager(platformContext, playgroundDatabase.imageQueries)
    val inMemoryHttpClient = HttpClient(inMemoryHttpClientEngine(), DefaultHttpConfiguration)
    val syncManager = SyncManager(inMemoryHttpClient, File::readChannel)
    val storageManager = StorageManager(PathProvider(platformContext))

    Circuit.Builder()
        .addCircuit<HomeScreen, HomeScreen.State>(
            presenterFactory = { _, navigator, _ ->
                presenterOf { HomePresenter(identityManager, navigator) }
            },
            uiFactory = { state, modifier ->
                HomeScreen(state, modifier, platformContext::reportFullyDrawn)
            },
        )
        .addCircuit<UpcomingEventsScreen, UpcomingEventsScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf { UpcomingEventsPresenter(eventPager) }
            },
            uiFactory = { state, modifier ->
                UpcomingEventsScreen(state, modifier)
            },
        )
        .addCircuit<GalleryScreen, GalleryScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf { GalleryPresenter(imageManager, syncManager) }
            },
            uiFactory = { state, modifier ->
                GalleryScreen(state, storageManager, modifier)
            },
        )
        .addCircuit<PastEventsScreen, PastEventsScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf {
                    PastEventsPresenter(
                        pastConferencesCallable = PastConferencesCallable(LocalHttpClient.current),
                        attendanceQueries = playgroundDatabase.attendanceQueries,
                        ioDispatcher = Dispatchers.IO,
                    )
                }
            },
            uiFactory = { state, modifier ->
                PastEventsScreen(state, modifier)
            },
        )
        .build()
}

private inline fun <reified S : Screen, UiState : CircuitUiState> Circuit.Builder.addCircuit(
    crossinline presenterFactory: (screen: S, navigator: Navigator, context: CircuitContext) -> Presenter<UiState>,
    crossinline uiFactory: @Composable (state: UiState, modifier: Modifier) -> Unit,
): Circuit.Builder = this
    .addPresenter(presenterFactory)
    .addUi<S, _>(uiFactory)
