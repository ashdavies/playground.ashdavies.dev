package io.ashdavies.tally.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.identity.IdentityState
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.tally.activity.FullyDrawnReporter
import io.ashdavies.tally.adaptive.ListDetailScaffoldScreen
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ashdavies.tally.gallery.GalleryScreen
import io.ashdavies.tally.material.icons.EventList
import io.ashdavies.tally.material.icons.EventUpcoming
import io.ashdavies.tally.past.PastScreen
import io.ashdavies.tally.routes.RoutesScreen
import io.ashdavies.tally.upcoming.UpcomingScreen

@Parcelize
internal object BottomBarScaffoldScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        data class ChildNav(val navEvent: NavEvent) : Event
        data class BottomNav(val screen: Screen) : Event

        data object Login : Event
    }

    data class State(
        val screen: Screen,
        val identityState: IdentityState,
        val isGalleryEnabled: Boolean,
        val isRoutesEnabled: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@CircuitScreenKey(BottomBarScaffoldScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class BottomBarScaffoldUi @Inject constructor(
    private val fullyDrawnReporter: FullyDrawnReporter,
) : Ui<BottomBarScaffoldScreen.State> {

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content(state: BottomBarScaffoldScreen.State, modifier: Modifier) {
        val eventSink = state.eventSink

        Scaffold(
            modifier = modifier,
            bottomBar = {
                BottomBar(
                    onClick = { eventSink(BottomBarScaffoldScreen.Event.BottomNav(it)) },
                    selected = state.screen,
                    isGalleryEnabled = state.isGalleryEnabled,
                    isRoutesEnabled = state.isRoutesEnabled,
                )
            },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
                insets = TopAppBarDefaults.windowInsets,
            ),
        ) { contentPadding ->
            CircuitContent(
                screen = state.screen,
                modifier = Modifier.padding(contentPadding),
                onNavEvent = { event ->
                    eventSink(BottomBarScaffoldScreen.Event.ChildNav(event))
                },
            )
        }

        LaunchedEffect(Unit) {
            fullyDrawnReporter.reportFullyDrawn()
        }
    }
}

@Composable
private fun BottomBar(
    onClick: (Screen) -> Unit,
    selected: Screen,
    isGalleryEnabled: Boolean,
    isRoutesEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(modifier) {
        NavigationBar {
            NavigationBarItem(
                selected = selected is ListDetailScaffoldScreen,
                onClick = { onClick(ListDetailScaffoldScreen(UpcomingScreen)) },
                icon = { NavigationBarImage(Icons.Outlined.EventUpcoming) },
            )

            if (isGalleryEnabled) {
                NavigationBarItem(
                    selected = selected is GalleryScreen,
                    onClick = { onClick(GalleryScreen) },
                    icon = { NavigationBarImage(Icons.Outlined.PhotoLibrary) },
                )
            }

            if (isRoutesEnabled) {
                NavigationBarItem(
                    selected = selected is RoutesScreen,
                    onClick = { onClick(RoutesScreen) },
                    icon = { NavigationBarImage(Icons.Outlined.Route) },
                )
            }

            NavigationBarItem(
                selected = selected is PastScreen,
                onClick = { onClick(PastScreen) },
                icon = { NavigationBarImage(Icons.Outlined.EventList) },
            )
        }
    }
}

@Composable
private fun NavigationBarImage(imageVector: ImageVector) {
    Image(
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        contentDescription = null,
        imageVector = imageVector,
    )
}
