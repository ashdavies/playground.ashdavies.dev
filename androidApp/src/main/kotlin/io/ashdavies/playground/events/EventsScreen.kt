package io.ashdavies.playground.events

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import io.ashdavies.playground.R
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
import io.ashdavies.playground.lifecycle.graphViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.datetime.LocalDate
import kotlin.random.Random

@Preview
@Composable
@OptIn(FlowPreview::class)
internal fun EventsScreen(context: Context = LocalContext.current) {
    val viewModel: EventsViewModel = context.graphViewModel {
        EventsViewModel(EventsStore())
    }

    EventsScreen(viewModel = viewModel)
}

@Composable
internal fun EventsScreen(viewModel: EventsViewModel) {
    val viewState: EventsViewState by viewModel
        .viewState
        .collectAsState(Uninitialised)

    EventsList(viewState)
}

@Composable
internal fun EventsList(viewState: EventsViewState) {
    when (viewState) {
        is Loading -> EventsList(List(10) { null })
        is Success -> EventsList(viewState.data)
        is Failure -> EventFailure(viewState.message)
        else -> Unit
    }
}

@Composable
internal fun EventsList(data: List<Section?>) {
    LazyColumn(
        contentPadding = LocalScaffoldPadding.current,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        items(data) { EventSection(it) }
    }
}

@Composable
internal fun EventSection(section: Section?) {
    when (section) {
        is Header -> EventHeader(date = section.date)
        is Item -> EventItem(data = section.data)
        null -> when (Random.nextBoolean()) {
            true -> EventHeader(date = null)
            false -> EventItem(data = null)
        }
    }
}

@Composable
internal fun EventHeader(date: LocalDate?) {
    Box(modifier = Modifier.padding(bottom = 12.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = when (date) {
                    null -> emptyString()
                    else -> stringResource(R.string.header, date.toCalendar())
                },
                style = MaterialTheme.typography.h5,
                modifier = Modifier.placeholder(
                    highlight = PlaceholderHighlight.shimmer(),
                    visible = date == null,
                ),
            )
        }
    }
}

@Composable
internal fun EventItem(data: Event?) {
    Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp)) {
        Surface(elevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp, 12.dp)) {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = data?.name ?: emptyString(),
                    modifier = Modifier.placeholder(
                        highlight = PlaceholderHighlight.shimmer(),
                        visible = data == null,
                    ),
                )

                Text(
                    style = MaterialTheme.typography.body2,
                    text = data?.location ?: emptyString(),
                    modifier = Modifier.placeholder(
                        highlight = PlaceholderHighlight.shimmer(),
                        visible = data == null,
                    ),
                )

                Text(
                    style = MaterialTheme.typography.caption,
                    text = data?.dateStart ?: emptyString(),
                    modifier = Modifier.placeholder(
                        highlight = PlaceholderHighlight.shimmer(),
                        visible = data == null,
                    ),
                )
            }
        }
    }
}

@Composable
internal fun EventFailure(message: String) {
    Text(
        modifier = Modifier.padding(16.dp, 12.dp),
        text = message,
    )
}
