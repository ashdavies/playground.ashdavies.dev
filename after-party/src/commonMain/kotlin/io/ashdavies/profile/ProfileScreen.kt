package io.ashdavies.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.android.FlowRow
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.placeholder.PlaceholderHighlight
import io.ashdavies.placeholder.fade
import io.ashdavies.placeholder.placeholder
import kotlin.random.Random.Default.nextInt

@Parcelize
internal object ProfileScreen : Screen {
    internal sealed interface Event : CircuitUiEvent {
        data object Login : Event
    }

    internal data class State(
        val profile: Profile? = null,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ProfileScreen(state: ProfileScreen.State, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(title = { Text("Profile") }) },
    ) { contentPadding ->
        ProfileScreen(
            onLogin = { eventSink(ProfileScreen.Event.Login) },
            modifier = Modifier.padding(contentPadding),
            state = state,
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileScreen(
    state: ProfileScreen.State,
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
) {
    Box(modifier) {
        Column(Modifier.padding(16.dp)) {
            when (state.profile) {
                null -> LoggedOutScreen(onLogin)
                else -> {
                    ProfileHeader(state.profile.name)
                    StubLanyardFlow()
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(top = 64.dp, bottom = 12.dp)
            .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(4.dp, 64.dp, 4.dp, 4.dp),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }

    Text(
        modifier = Modifier.padding(bottom = 4.dp),
        style = MaterialTheme.typography.headlineSmall,
        text = "Events",
    )
}

@Composable
private fun StubLanyardFlow(until: Int = 30) {
    FlowRow {
        repeat(nextInt(until)) {
            Box(
                modifier = Modifier
                    .background(Color.DarkGray)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .size(64.dp, 64.dp)
                    .clip(CircleShape)
                    .placeholder(true, highlight = PlaceholderHighlight.fade())
                    .padding(4.dp),
            )
        }
    }
}

@Composable
private fun LoggedOutScreen(onLogin: () -> Unit) {
    Column {
        Text("Not logged in")

        Button(onClick = onLogin) {
            Text("Login")
        }
    }
}
