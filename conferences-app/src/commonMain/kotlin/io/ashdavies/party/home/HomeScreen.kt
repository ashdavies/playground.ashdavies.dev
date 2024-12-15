package io.ashdavies.party.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.identity.IdentityState
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.party.animation.FadeVisibility
import io.ashdavies.party.config.booleanConfigAsState
import io.ashdavies.party.material.icons.EventList
import io.ashdavies.party.material.icons.EventUpcoming
import io.ashdavies.party.past.GalleryScreen
import io.ashdavies.party.upcoming.UpcomingEventsScreen

@Parcelize
internal object HomeScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        data class ChildNav(val navEvent: NavEvent) : Event
        data class BottomNav(val screen: Screen) : Event

        data object Login : Event
    }

    data class State(
        val identityState: IdentityState,
        val screen: Screen,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
    reportFullyDrawn: () -> Unit,
) {
    val showPastEvents by booleanConfigAsState { true }
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        bottomBar = {
            FadeVisibility(showPastEvents) {
                HomeBottomBar { screen ->
                    eventSink(HomeScreen.Event.BottomNav(screen))
                }
            }
        },
        floatingActionButton = { },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
            insets = TopAppBarDefaults.windowInsets,
        ),
    ) { contentPadding ->
        CircuitContent(
            screen = state.screen,
            modifier = Modifier.padding(contentPadding),
            onNavEvent = { event ->
                eventSink(HomeScreen.Event.ChildNav(event))
            },
        )
    }

    val latestReportFullyDrawn by rememberUpdatedState(reportFullyDrawn)

    LaunchedEffect(Unit) {
        latestReportFullyDrawn()
    }
}

@Composable
internal fun HomeBottomBar(
    modifier: Modifier = Modifier,
    selected: Screen = HomeScreen,
    onClick: (Screen) -> Unit = { },
) {
    BottomAppBar(modifier) {
        NavigationBar {
            NavigationBarItem(
                selected = selected is UpcomingEventsScreen,
                onClick = { onClick(UpcomingEventsScreen) },
                icon = { NavigationBarImage(Icons.Outlined.EventUpcoming) },
            )

            NavigationBarItem(
                selected = selected is GalleryScreen,
                onClick = { onClick(GalleryScreen) },
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
