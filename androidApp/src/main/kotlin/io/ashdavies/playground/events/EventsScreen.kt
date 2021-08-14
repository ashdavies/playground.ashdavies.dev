package io.ashdavies.playground.events

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import io.ashdavies.playground.R
import io.ashdavies.playground.arch.collectViewState
import io.ashdavies.playground.arch.getViewStateStore
import io.ashdavies.playground.arch.viewModel
import io.ashdavies.playground.compose.fade
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.datetime.toCalendar
import io.ashdavies.playground.emptyString
import io.ashdavies.playground.events.EventsViewState.Failure
import io.ashdavies.playground.events.EventsViewState.Loading
import io.ashdavies.playground.events.EventsViewState.Section
import io.ashdavies.playground.events.EventsViewState.Section.Header
import io.ashdavies.playground.events.EventsViewState.Section.Item
import io.ashdavies.playground.events.EventsViewState.Success
import io.ashdavies.playground.events.EventsViewState.Uninitialised
import io.ashdavies.playground.graph
import kotlinx.datetime.LocalDate
import kotlin.random.Random

@Preview
@Composable
internal fun EventsScreen(context: Context = LocalContext.current) = graph(context) {
    val viewModel: EventsViewModel = viewModel { EventsViewModel { EventsStore() } }
    val viewState: EventsViewState by viewModel
        .getViewStateStore { Uninitialised }
        .collectViewState()

    when (val it: EventsViewState = viewState) {
        is Loading -> EventsList(List(10) { null }) {}
        is Failure -> EventFailure(it.message)
        is Success -> EventsList(it.data) {
            println("Clicked $it")
        }
        else -> Unit
    }
}

@Composable
internal fun EventsList(data: List<Section?>, onClick: (Section) -> Unit) {
    LazyColumn(
        contentPadding = LocalScaffoldPadding.current,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        items(data) { EventSection(it, onClick) }
    }
}

@Composable
internal fun EventSection(section: Section?, onClick: (Section) -> Unit) {
    when (section) {
        is Header -> EventHeader(date = section.date)
        is Item -> EventItem(data = section.data) { onClick(section) }
        null -> when (Random.nextBoolean()) {
            true -> EventHeader(date = null)
            false -> EventItem(data = null) {}
        }
    }
}

@Composable
internal fun EventHeader(date: LocalDate?) {
    Box(modifier = Modifier.padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            PlaceholderText(
                text = date?.let { stringResource(R.string.header, it.toCalendar()) },
                style = MaterialTheme.typography.h5,
            )
        }
    }
}

@Composable
internal fun EventItem(data: Event?, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)) {
        Button(onClick = onClick) {
            PlaceholderText(
                style = MaterialTheme.typography.body1,
                text = data?.name,
            )

            PlaceholderText(
                style = MaterialTheme.typography.body2,
                text = data?.location,
            )

            PlaceholderText(
                style = MaterialTheme.typography.caption,
                text = data?.dateStart,
            )
        }
    }
}

@Composable
private fun PlaceholderText(text: String?, style: TextStyle = LocalTextStyle.current) {
    Text(
        style = style,
        text = text ?: emptyString(),
        modifier = Modifier
            .defaultMinSize(minWidth = Dp(style.fontSize.value * 12))
            .padding(bottom = 2.dp)
            .fade(text == null),
    )
}


@Composable
internal fun EventFailure(message: String) {
    Text(
        modifier = Modifier.padding(16.dp, 12.dp),
        text = message,
    )
}
