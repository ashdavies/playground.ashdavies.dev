package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.ashdavies.asg.AsgConference
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@Preview
@Composable
@PreviewTest
@OptIn(ExperimentalSerializationApi::class)
public fun EventListUiPreview() {
    EventListUi(
        state = EventListState(
            itemList = Json
                .decodeFromString<List<AsgConference>>(
                    AsgConference::class.java.classLoader!!
                        .getResourceAsStream("upcoming.json")
                        .bufferedReader()
                        .readText(),
                )
                .mapIndexed { index, item -> item.toEvent(index.toLong()) }
                .toImmutableList(),
            selectedIndex = null,
            isRefreshing = false,
            errorMessage = null,
            eventSink = { },
        ),
    )
}
