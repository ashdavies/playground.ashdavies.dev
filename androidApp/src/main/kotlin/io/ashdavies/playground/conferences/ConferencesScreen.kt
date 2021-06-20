package io.ashdavies.playground.conferences

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
import io.ashdavies.playground.conferences.ConferencesViewState.Failure
import io.ashdavies.playground.conferences.ConferencesViewState.Loading
import io.ashdavies.playground.conferences.ConferencesViewState.Section
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Header
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.conferences.ConferencesViewState.Success
import io.ashdavies.playground.conferences.ConferencesViewState.Uninitialised
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.datetime.toCalendar
import io.ashdavies.playground.emptyString
import io.ashdavies.playground.lifecycle.graphViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.datetime.LocalDate
import kotlin.random.Random

@Preview
@Composable
@OptIn(FlowPreview::class)
internal fun ConferencesScreen(context: Context = LocalContext.current) {
    val viewModel: ConferencesViewModel = context.graphViewModel {
        ConferencesViewModel(ConferencesStore())
    }

    ConferencesScreen(viewModel = viewModel)
}

@Composable
internal fun ConferencesScreen(viewModel: ConferencesViewModel) {
    val viewState: ConferencesViewState by viewModel
        .viewState
        .collectAsState(Uninitialised)

    ConferencesList(viewState)
}

@Composable
internal fun ConferencesList(viewState: ConferencesViewState) {
    when (viewState) {
        is Loading -> ConferencesList(List(10) { null })
        is Success -> ConferencesList(viewState.data)
        is Failure -> ConferencesFailure(viewState.message)
        else -> Unit
    }
}

@Composable
internal fun ConferencesList(data: List<Section?>) {
    LazyColumn(
        contentPadding = LocalScaffoldPadding.current,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        items(data) { ConferenceSection(it) }
    }
}

@Composable
internal fun ConferenceSection(section: Section?) {
    when (section) {
        is Header -> ConferenceHeader(date = section.date)
        is Item -> ConferenceItem(data = section.data)
        null -> when (Random.nextBoolean()) {
            true -> ConferenceHeader(date = null)
            false -> ConferenceItem(data = null)
        }
    }
}

@Composable
internal fun ConferenceHeader(date: LocalDate?) {
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
internal fun ConferenceItem(data: Conference?) {
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
internal fun ConferencesFailure(message: String) {
    Text(
        modifier = Modifier.padding(16.dp, 12.dp),
        text = message,
    )
}
