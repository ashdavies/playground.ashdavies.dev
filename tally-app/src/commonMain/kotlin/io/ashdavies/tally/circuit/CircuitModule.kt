package io.ashdavies.tally.circuit

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.events.paging.EventPager
import io.ashdavies.tally.events.paging.PagedUpcomingEventsCallable
import io.ashdavies.tally.gallery.GalleryModule
import io.ashdavies.tally.gallery.PathProvider
import io.ashdavies.tally.gallery.StorageManager
import io.ashdavies.tally.home.HomeModule
import io.ashdavies.tally.past.PastModule
import io.ashdavies.tally.upcoming.UpcomingModule
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers

internal object CircuitModule {

    fun circuit(
        playgroundDatabase: PlaygroundDatabase,
        platformContext: PlatformContext,
        httpClient: HttpClient,
        windowSizeClass: WindowSizeClass,
    ): Circuit {
        val remoteAnalytics = RemoteAnalytics()
        val storageManager = StorageManager(
            platformContext = platformContext,
            pathProvider = PathProvider(platformContext),
            coroutineContext = Dispatchers.IO,
        )

        val eventPager = EventPager(
            eventsCallable = PagedUpcomingEventsCallable(
                httpClient = httpClient,
                remoteConfig = RemoteConfig(),
            ),
            eventsQueries = playgroundDatabase.eventsQueries,
        )

        return Circuit.Builder()
            .addPresenterFactory(
                HomeModule.homePresenterFactory(
                    platformContext = platformContext,
                    credentialQueries = playgroundDatabase.credentialQueries,
                ),
                UpcomingModule.upcomingPresenterFactory(
                    eventPager = eventPager,
                    remoteAnalytics = remoteAnalytics,
                ),
                GalleryModule.galleryPresenterFactory(
                    storageManager = storageManager,
                    imageQueries = playgroundDatabase.imageQueries,
                    remoteAnalytics = remoteAnalytics,
                ),
                PastModule.pastPresenterFactory(
                    httpClient = httpClient,
                    attendanceQueries = playgroundDatabase.attendanceQueries,
                ),
            )
            .addUiFactory(
                HomeModule.homeUiFactory(
                    platformContext = platformContext,
                ),
                UpcomingModule.upcomingUiFactory(
                    windowSizeClass = windowSizeClass,
                ),
                GalleryModule.galleryUiFactory(
                    storageManager = storageManager,
                ),
                PastModule.pastUiFactory(
                    windowSizeClass = windowSizeClass,
                ),
            )
            .build()
    }
}

internal inline fun <reified S : Screen, UiState : CircuitUiState> presenterFactoryOf(
    crossinline body: @Composable (Navigator, CircuitContext) -> UiState,
): Presenter.Factory = Presenter.Factory { screen, navigator, context ->
    when (screen) {
        is S -> presenterOf { body(navigator, context) }
        else -> null
    }
}

internal inline fun <reified S : Screen, UiState : CircuitUiState> uiFactoryOf(
    crossinline body: @Composable (state: UiState, modifier: Modifier) -> Unit,
): Ui.Factory = Ui.Factory { screen, context ->
    when (screen) {
        is S -> ui<UiState> { state, modifier -> body(state, modifier) }
        else -> null
    }
}
