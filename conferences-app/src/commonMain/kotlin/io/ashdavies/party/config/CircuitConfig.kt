package io.ashdavies.party.config

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
import io.ashdavies.party.coroutines.rememberRetainedCoroutineScope
import io.ashdavies.party.events.paging.rememberEventPager
import io.ashdavies.party.gallery.File
import io.ashdavies.party.gallery.GalleryPresenter
import io.ashdavies.party.gallery.GalleryScreen
import io.ashdavies.party.gallery.ImageManager
import io.ashdavies.party.gallery.PathProvider
import io.ashdavies.party.gallery.StorageManager
import io.ashdavies.party.gallery.SyncManager
import io.ashdavies.party.gallery.inMemoryHttpClientEngine
import io.ashdavies.party.gallery.readChannel
import io.ashdavies.party.home.HomePresenter
import io.ashdavies.party.home.HomeScreen
import io.ashdavies.party.past.PastEventsPresenter
import io.ashdavies.party.past.PastEventsScreen
import io.ashdavies.party.upcoming.UpcomingEventsPresenter
import io.ashdavies.party.upcoming.UpcomingEventsScreen
import io.ashdavies.playground.PlaygroundDatabase
import io.ashdavies.sql.LocalTransacter
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import io.ashdavies.party.events.Event as DatabaseEvent

@Composable
public fun rememberCircuit(
    platformContext: PlatformContext,
    eventPager: Pager<String, DatabaseEvent> = rememberEventPager(),
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
): Circuit = remember(platformContext) {
    val identityManager = IdentityManager(platformContext, playgroundDatabase.credentialQueries)
    val imageManager = ImageManager(platformContext, playgroundDatabase.imageQueries)
    val inMemoryHttpClient = HttpClient(inMemoryHttpClientEngine(), DefaultHttpConfiguration)
    val syncManager = SyncManager(inMemoryHttpClient, File::readChannel)
    val storageManager = StorageManager(PathProvider(platformContext))

    Circuit.Builder()
        .addCircuit<HomeScreen, HomeScreen.State>(
            presenterFactory = { _, navigator, _ ->
                presenterOf { HomePresenter(identityManager, coroutineScope, navigator) }
            },
            uiFactory = { state, modifier ->
                HomeScreen(state, modifier, platformContext::reportFullyDrawn)
            },
        )
        .addCircuit<UpcomingEventsScreen, UpcomingEventsScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf { UpcomingEventsPresenter(eventPager, coroutineScope) }
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
