package io.ashdavies.party.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import okio.ByteString.Companion.encode

@Composable
internal fun EventsDetailPane(
    event: Event,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { EventsTopBar(event.name) },
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Card(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
            ) {
                EventsDetailImage(
                    imageUrl = event.imageUrl,
                    name = event.name,
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            ) {
                EventsDetailDate(
                    dateStart = event.dateStart,
                    dateEnd = event.dateEnd,
                )

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
private fun EventsDetailDate(
    dateStart: String,
    dateEnd: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Event,
            contentDescription = null,
            modifier = Modifier.padding(16.dp),
        )

        Column {
            Text("$dateStart - $dateEnd")
        }
    }
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
