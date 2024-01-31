package io.ashdavies.party

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.activity.ActivityScreen
import io.ashdavies.gallery.GalleryScreen
import io.ashdavies.gallery.GallerySheetContent
import io.ashdavies.identity.IdentityState
import io.ashdavies.material.BottomSheetScaffold
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.profile.ProfileScreen

public fun AfterPartyScreen(): Screen = AfterPartyScreen

@Parcelize
internal object AfterPartyScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        data class ChildNav(val navEvent: NavEvent) : Event
        data class BottomNav(val screen: Screen) : Event
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
    val eventSink = state.eventSink

    BottomSheetScaffold(
        sheetContent = { GallerySheetContent({ }) },
        showDragHandle = false,
        topBar = {
            AfterPartyTopBar(
                actions = {
                    ProfileActionButton(
                        identityState = state.identityState,
                        onClick = { eventSink(AfterPartyScreen.Event.BottomNav(ProfileScreen)) },
                    )
                },
            )
        },
        bottomBar = {
            AfterPartyBottomBar { screen ->
                eventSink(AfterPartyScreen.Event.BottomNav(screen))
            }
        },
        floatingActionButton = { },
        isExpanded = false,
        modifier = modifier,
    ) {
        CircuitContent(
            screen = state.screen,
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
                icon = { NavigationBarImage(Icons.Default.Home) },
                onClick = { onClick(ActivityScreen) },
                selected = selected is ActivityScreen,
            )

            NavigationBarItem(
                icon = { NavigationBarImage(Icons.Default.Edit) },
                onClick = { onClick(GalleryScreen) },
                selected = selected is GalleryScreen,
            )

            NavigationBarItem(
                icon = { NavigationBarImage(Icons.Default.Person) },
                onClick = { onClick(ProfileScreen) },
                selected = selected is ProfileScreen,
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
