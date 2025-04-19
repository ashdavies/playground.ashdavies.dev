package io.ashdavies.tally.circuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.aggregator.PastConferencesCallable
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.tally.gallery.GalleryPresenter
import io.ashdavies.tally.gallery.GalleryScreen
import io.ashdavies.tally.home.HomePresenter
import io.ashdavies.tally.home.HomeScreen
import io.ashdavies.tally.metro.TallyAppGraph
import io.ashdavies.tally.past.PastEventsPresenter
import io.ashdavies.tally.past.PastEventsScreen
import io.ashdavies.tally.upcoming.UpcomingEventsPresenter
import io.ashdavies.tally.upcoming.UpcomingEventsScreen
import kotlinx.coroutines.Dispatchers

@Composable
internal fun rememberCircuit(appGraph: TallyAppGraph): Circuit = remember(appGraph) {
    Circuit.Builder()
        .addCircuit<HomeScreen, HomeScreen.State>(
            presenterFactory = { _, navigator, _ ->
                presenterOf { HomePresenter(appGraph.identityManager, navigator) }
            },
            uiFactory = { state, modifier ->
                HomeScreen(state, modifier, appGraph.drawnReporter::reportFullyDrawn)
            },
        )
        .addCircuit<UpcomingEventsScreen, UpcomingEventsScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf { UpcomingEventsPresenter(appGraph.eventPager) }
            },
            uiFactory = { state, modifier ->
                UpcomingEventsScreen(state, modifier)
            },
        )
        .addCircuit<GalleryScreen, GalleryScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf { GalleryPresenter(appGraph.imageManager, appGraph.syncManager) }
            },
            uiFactory = { state, modifier ->
                GalleryScreen(state, appGraph.storageManager, modifier)
            },
        )
        .addCircuit<PastEventsScreen, PastEventsScreen.State>(
            presenterFactory = { _, _, _ ->
                presenterOf {
                    PastEventsPresenter(
                        pastConferencesCallable = appGraph.pastPreferencesCallable,
                        attendanceQueries = appGraph.attendanceQueriesProvider(),
                        ioDispatcher = appGraph.ioDispatcher,
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
