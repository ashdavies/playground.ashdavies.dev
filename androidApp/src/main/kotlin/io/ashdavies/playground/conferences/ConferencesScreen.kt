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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.conferences.ConferencesViewModel.Companion.Factory
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Header
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferenceFactory
import java.lang.System.currentTimeMillis
import java.text.DateFormat
import java.text.SimpleDateFormat

private val dateFormat: DateFormat = SimpleDateFormat("MMMM, yyyy")

@Preview
@Composable
internal fun ConferencesPreview() {
    val conferenceFactory = ConferenceFactory { currentTimeMillis() }
    val conferenceList = List(10) { Item(conferenceFactory()) }

    ConferencesList(ConferencesViewState.Success(conferenceList))
}

@Composable
internal fun ConferencesScreen(context: Context = ContextAmbient.current) {
    val viewModel: ConferencesViewModel = viewModel(factory = Factory(context))
    val state: State<ConferencesViewState> = viewModel
        .viewState
        .collectAsState()

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
        is Header -> ConferenceHeader(timeInMillis = section.timeInMillis)
        is Item -> ConferenceItem(data = section.data)
    }
}

@Composable
internal fun ConferenceHeader(timeInMillis: Long) {
    Box(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = dateFormat.format(timeInMillis),
            style = MaterialTheme.typography.h5,
        )
    }
}

@Composable
internal fun ConferenceItem(data: Conference) {
    Box(modifier = Modifier.padding(bottom = 12.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp, 12.dp)) {
                Text(text = data.name)
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
