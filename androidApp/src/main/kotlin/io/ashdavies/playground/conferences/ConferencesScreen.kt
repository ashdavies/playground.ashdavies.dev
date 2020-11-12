package io.ashdavies.playground.conferences

import android.content.Context
import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import io.ashdavies.playground.R
import io.ashdavies.playground.conferences.ConferencesViewModel.Companion.Factory
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Header
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.ktx.toCalendar
import kotlinx.datetime.LocalDate

@Composable
internal fun ConferencesScreen(context: Context = ContextAmbient.current) {
    val viewModel: ConferencesViewModel = viewModel(factory = Factory(context))
    val state: State<ConferencesViewState> = viewModel
        .viewState
        .collectAsState(ConferencesViewState.Uninitialised)

    ConferencesList(state.value)
}

@Composable
internal fun ConferencesList(viewState: ConferencesViewState) {
    when (viewState) {
        is ConferencesViewState.Loading -> ConferencesLoading()
        is ConferencesViewState.Success -> ConferencesList(viewState.data)
        is ConferencesViewState.Failure -> ConferencesFailure(viewState.message)
        else -> Unit
    }
}

@Composable
internal fun ConferencesList(data: List<ConferencesViewState.Section>) {
    LazyColumnFor(
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 0.dp),
        itemContent = { ConferenceSection(it) },
        items = data,
    )
}

@Composable
internal fun ConferenceSection(section: ConferencesViewState.Section) {
    when (section) {
        is Header -> ConferenceHeader(date = section.date)
        is Item -> ConferenceItem(data = section.data)
    }
}

@Composable
internal fun ConferenceHeader(date: LocalDate) {
    Box(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = stringResource(R.string.header, date.toCalendar()),
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
internal fun ConferenceItem(data: Conference) {
    Box(modifier = Modifier.padding(bottom = 12.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp, 12.dp)) {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = data.name,
                )

                Text(
                    style = MaterialTheme.typography.body2,
                    text = data.location,
                )

                Text(
                    style = MaterialTheme.typography.caption,
                    text = data.dateStart.toString(),
                )
            }
        }
    }
}

@Composable
internal fun ConferencesLoading() {
    Column(modifier = Modifier.fillMaxWidth()) {
        val modifier = Modifier
            .align(CenterHorizontally)
            .fillMaxHeight()

        Row(modifier = modifier) {
            CircularProgressIndicator(modifier = Modifier.align(CenterVertically))
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
