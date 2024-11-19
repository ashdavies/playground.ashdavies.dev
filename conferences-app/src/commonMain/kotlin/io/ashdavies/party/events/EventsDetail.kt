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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
internal fun EventsDetail(
    event: Event,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        AsyncImage(
            model = event.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .height(300.dp),
            placeholder = null,
        )

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Event,
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
            )

            Column {
                Text("${event.dateStart} - ${event.dateEnd}")
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.MyLocation,
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
            )

            Column {
                Text(event.location)
            }
        }
    }
}
