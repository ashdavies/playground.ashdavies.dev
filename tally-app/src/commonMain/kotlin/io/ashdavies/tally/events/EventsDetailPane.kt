package io.ashdavies.tally.events

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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.ashdavies.tally.material.BackButton
import io.ashdavies.tally.material.LocalWindowSizeClass
import io.ashdavies.tally.material.padding
import io.ashdavies.tally.material.spacing
import kotlinx.datetime.LocalDate
import okio.ByteString.Companion.encode
import org.jetbrains.compose.resources.stringResource
import playground.tally_app.generated.resources.Res
import playground.tally_app.generated.resources.call_for_papers_closed
import playground.tally_app.generated.resources.call_for_papers_days_remaining

@Composable
internal fun EventsDetailPane(
    item: Event,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = LocalWindowSizeClass.current,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EventsTopBar(
                title = item.name,
                actions = {
                    IconButton(onClick = { error("Crashlytics") }) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    }
                },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        BackButton(onBackClick)
                    }
                },
            )
        },
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Card(Modifier.padding(MaterialTheme.spacing.large)) {
                Box {
                    EventsDetailImage(
                        imageUrl = item.imageUrl,
                        name = item.name,
                    )

                    EventDateLabel(
                        dateStart = remember { LocalDate.parse(item.dateStart) },
                        dateEnd = remember { LocalDate.parse(item.dateEnd) },
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.large)
                            .align(Alignment.TopEnd),
                    )
                }
            }

            EventsDetailLocation(
                location = item.location,
            )

            if (item.cfpEnd != null) {
                EventsDetailCfp(
                    cfpEnd = item.cfpEnd,
                    cfpSite = item.cfpSite,
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
    Card(
        modifier = modifier
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth(),
    ) {
        Row(
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
}

@Composable
private fun EventsDetailCfp(
    cfpSite: String?,
    cfpEnd: String,
    modifier: Modifier = Modifier,
) {
    val daysUntilCfpEnd = daysUntilCfpEnd(LocalDate.parse(cfpEnd))
    val uriHandler = LocalUriHandler.current
    val newModifier = modifier
        .padding(MaterialTheme.spacing.large)
        .fillMaxWidth()

    when {
        daysUntilCfpEnd > 0 && cfpSite != null -> Card(
            onClick = { uriHandler.openUri(cfpSite) },
            modifier = newModifier,
        ) {
            Text(
                text = stringResource(Res.string.call_for_papers_days_remaining, daysUntilCfpEnd),
                modifier = Modifier.padding(16.dp),
            )
        }

        else -> Card(newModifier) {
            Text(
                text = stringResource(Res.string.call_for_papers_closed),
                modifier = Modifier.padding(16.dp),
            )
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
