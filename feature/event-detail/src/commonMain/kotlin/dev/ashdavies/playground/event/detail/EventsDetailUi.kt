package dev.ashdavies.playground.event.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.valentinilk.shimmer.shimmer
import dev.ashdavies.identity.IdentityState
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.ui.BackButton
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.DateRangeBadge
import dev.ashdavies.playground.ui.DateRangeBadgeState
import dev.ashdavies.playground.ui.ProfileActionButton
import dev.ashdavies.playground.ui.emptyString
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import playground.feature.event_detail.generated.resources.Res
import playground.feature.event_detail.generated.resources.call_for_papers_closed
import playground.feature.event_detail.generated.resources.call_for_papers_days_remaining

@Composable
@CircuitInject(EventScreen.Detail::class, AppScope::class)
public fun EventsDetailUi(state: EventDetailState, modifier: Modifier = Modifier) {
    val itemOrNull = (state.itemState as? EventDetailState.ItemState.Done)?.item
    val isLoading = state.itemState is EventDetailState.ItemState.Loading
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = itemOrNull?.name ?: emptyString(),
                navigationIcon = { BackButton(state.onBackPressed) },
                actions = {
                    ProfileActionButton(
                        identityState = IdentityState.Unsupported,
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Unsupported Platform")
                            }
                        },
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .then(if (isLoading) Modifier.shimmer() else Modifier)
                .padding(contentPadding),
        ) {
            Card(Modifier.padding(MaterialTheme.spacing.large)) {
                Box(if (itemOrNull != null) Modifier.shimmer() else Modifier) {
                    EventsDetailImage(
                        imageUrl = itemOrNull?.imageUrl,
                        backgroundSeed = itemOrNull?.location ?: emptyString(),
                    )

                    if (itemOrNull != null) {
                        DateRangeBadge(
                            state = remember(itemOrNull.dateStart, itemOrNull.dateEnd) {
                                DateRangeBadgeState(
                                    dateStart = LocalDate.parse(itemOrNull.dateStart),
                                    dateEnd = LocalDate.parse(itemOrNull.dateEnd),
                                )
                            },
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.large)
                                .align(Alignment.TopEnd),
                        )
                    }
                }
            }

            EventsDetailLocation(itemOrNull?.location ?: emptyString())

            itemOrNull?.cfpEnd?.let { cfpEnd ->
                EventsDetailCfp(
                    cfpSite = itemOrNull.cfpSite,
                    cfpEnd = cfpEnd,
                )
            }
        }
    }
}

@Composable
private fun EventsDetailImage(
    imageUrl: String?,
    backgroundSeed: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .background(randomColor(backgroundSeed))
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

private const val COLOR_COMPONENTS = 3

private fun randomColor(seed: String) = when {
    seed.length < COLOR_COMPONENTS -> Color.LightGray

    else -> Color(
        red = seed[0].code % 0xFF,
        green = seed[1].code % 0xFF,
        blue = seed[2].code % 0xFF,
    )
}
