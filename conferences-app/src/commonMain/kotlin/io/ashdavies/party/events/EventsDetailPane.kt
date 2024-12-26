package io.ashdavies.party.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.ashdavies.party.material.padding
import io.ashdavies.party.material.spacing
import kotlinx.datetime.LocalDate
import okio.ByteString.Companion.encode

@Composable
internal fun EventsDetailPane(
    event: Event,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EventsTopBar(
                title = event.name,
                actions = {
                    IconButton(onClick = { error("Crashlytics") }) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    }
                },
            )
        },
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Card(Modifier.padding(MaterialTheme.spacing.large)) {
                Box {
                    EventsDetailImage(
                        imageUrl = event.imageUrl,
                        name = event.name,
                    )

                    EventDateLabel(
                        dateStart = remember { LocalDate.parse(event.dateStart) },
                        dateEnd = remember { LocalDate.parse(event.dateEnd) },
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.large)
                            .align(Alignment.TopEnd),
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
                    .fillMaxWidth(),
            ) {
                EventsDetailLocation(
                    location = event.location,
                )
            }
        }
    }
}

@Composable
private fun EventsDetailImage(
    imageUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .background(randomColor(name))
            .fillMaxWidth()
            .height(200.dp),
        placeholder = null,
    )
}

@Composable
private fun EventsDetailLocation(
    location: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.MyLocation,
            contentDescription = null,
            modifier = Modifier.padding(16.dp),
        )

        Column {
            Text(location)
        }
    }
}

private fun randomColor(seed: String) = with(seed.encode().md5()) {
    Color(
        red = get(0).toUByte().toInt(),
        green = get(1).toUByte().toInt(),
        blue = get(2).toUByte().toInt(),
    )
}
