package io.ashdavies.tally.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
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
import io.ashdavies.tally.gallery.GalleryScreen
import io.ashdavies.tally.material.icons.EventList
import io.ashdavies.tally.material.icons.EventUpcoming
import io.ashdavies.tally.past.PastScreen
import io.ashdavies.tally.upcoming.UpcomingScreen

@Parcelize
internal object HomeScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        data class ChildNav(val navEvent: NavEvent) : Event
        data class BottomNav(val screen: Screen) : Event

        data object Login : Event
    }

    data class State(
        val screen: Screen,
        val identityState: IdentityState,
        val isGalleryEnabled: Boolean,
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
    val latestReportFullyDrawn by rememberUpdatedState(reportFullyDrawn)
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        bottomBar = {
            HomeBottomBar(isGalleryEnabled = state.isGalleryEnabled) { screen ->
                eventSink(HomeScreen.Event.BottomNav(screen))
            }
        },
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

    LaunchedEffect(Unit) {
        latestReportFullyDrawn()
    }
}

@Composable
internal fun HomeBottomBar(
    modifier: Modifier = Modifier,
    selected: Screen = HomeScreen,
    isGalleryEnabled: Boolean = false,
    onClick: (Screen) -> Unit = { },
) {
    BottomAppBar(modifier) {
        NavigationBar {
            NavigationBarItem(
                selected = selected is UpcomingScreen,
                onClick = { onClick(UpcomingScreen) },
                icon = { NavigationBarImage(Icons.Outlined.EventUpcoming) },
            )

            if (isGalleryEnabled) {
                NavigationBarItem(
                    selected = selected is GalleryScreen,
                    onClick = { onClick(GalleryScreen) },
                    icon = { NavigationBarImage(Icons.Outlined.PhotoLibrary) },
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
