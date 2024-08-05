package io.ashdavies.party

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.analytics.OnClick
import io.ashdavies.animation.FadeVisibility
import io.ashdavies.events.EventsScreen
import io.ashdavies.gallery.GalleryScreen
import io.ashdavies.gallery.GallerySheetContent
import io.ashdavies.identity.IdentityState
import io.ashdavies.material.BottomSheetScaffold
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

public fun afterPartyScreen(): Screen = AfterPartyScreen

@Parcelize
internal object AfterPartyScreen : Parcelable, Screen {
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
internal fun AfterPartyScreen(
    state: AfterPartyScreen.State,
    modifier: Modifier = Modifier,
) {
    val isProfileEnabled by booleanConfigAsState { isProfileEnabled() }
    val isHomeEnabled by booleanConfigAsState { isHomeEnabled() }
    val eventSink = state.eventSink

    BottomSheetScaffold(
        sheetContent = { GallerySheetContent({ }) },
        showDragHandle = false,
        topBar = {
            AfterPartyTopBar(
                actions = {
                    FadeVisibility(isProfileEnabled) {
                        ProfileActionButton(
                            identityState = state.identityState,
                            onClick = OnClick("profile_login") {
                                eventSink(AfterPartyScreen.Event.Login)
                            },
                        )
                    }
                },
            )
        },
        bottomBar = {
            FadeVisibility(isHomeEnabled) {
                AfterPartyBottomBar { screen ->
                    eventSink(AfterPartyScreen.Event.BottomNav(screen))
                }
            }
        },
        floatingActionButton = { },
        isExpanded = false,
        modifier = modifier,
    ) { contentPadding ->
        CircuitContent(
            screen = state.screen,
            modifier = Modifier.padding(contentPadding),
            onNavEvent = { event ->
                eventSink(AfterPartyScreen.Event.ChildNav(event))
            },
        )
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun AfterPartyTopBar(
    title: String = "AfterParty",
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarState()),
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        actions = actions,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
internal fun AfterPartyBottomBar(
    selected: Screen = AfterPartyScreen,
    modifier: Modifier = Modifier,
    onClick: (Screen) -> Unit = { },
) {
    BottomAppBar(modifier) {
        NavigationBar {
            NavigationBarItem(
                selected = selected is GalleryScreen,
                onClick = { onClick(GalleryScreen) },
                icon = { NavigationBarImage(Icons.Default.Home) },
            )

            NavigationBarItem(
                selected = selected is EventsScreen,
                onClick = { onClick(EventsScreen) },
                icon = { NavigationBarImage(Icons.AutoMirrored.Filled.List) },
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
