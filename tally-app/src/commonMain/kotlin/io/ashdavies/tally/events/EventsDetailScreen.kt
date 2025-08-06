package io.ashdavies.tally.events

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.placeholder.material3.placeholder
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.ashdavies.identity.IdentityState
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ashdavies.tally.material.BackButton
import io.ashdavies.tally.material.padding
import io.ashdavies.tally.material.spacing
import io.ashdavies.tally.profile.ProfileActionButton
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import playground.tally_app.generated.resources.Res
import playground.tally_app.generated.resources.call_for_papers_closed
import playground.tally_app.generated.resources.call_for_papers_days_remaining

private const val PLACEHOLDER = ""

@Parcelize
internal data class EventsDetailScreen(val id: Long) : Parcelable, Screen {
    data class State(val itemState: ItemState, val onBackPressed: () -> Unit) : CircuitUiState {
        sealed class ItemState {
            data object Loading : ItemState()
            data class Done(val item: Event) : ItemState()
        }
    }
}

@CircuitScreenKey(EventsDetailScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class EventsDetailUi @Inject constructor() : Ui<EventsDetailScreen.State> {

    @Composable
    override fun Content(state: EventsDetailScreen.State, modifier: Modifier) {
        val itemOrNull = (state.itemState as? EventsDetailScreen.State.ItemState.Done)?.item
        val isLoading = state.itemState is EventsDetailScreen.State.ItemState.Loading

        Scaffold(
            modifier = modifier,
            topBar = {
                EventsTopBar(
                    title = itemOrNull?.name ?: PLACEHOLDER,
                    actions = {
                        ProfileActionButton(
                            identityState = IdentityState.Unsupported,
                            onClick = { error("Unsupported Platform") },
                        )
                    },
                    navigationIcon = {
                        BackButton(state.onBackPressed)
                    },
                )
            },
        ) { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                Card(Modifier.padding(MaterialTheme.spacing.large)) {
                    Box {
                        EventsDetailImage(
                            imageUrl = itemOrNull?.imageUrl,
                            backgroundSeed = itemOrNull?.location ?: PLACEHOLDER,
                            modifier = Modifier.placeholder(isLoading),
                        )

                        if (itemOrNull != null) {
                            EventDateLabel(
                                dateStart = remember { LocalDate.parse(itemOrNull.dateStart) },
                                dateEnd = remember { LocalDate.parse(itemOrNull.dateEnd) },
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.large)
                                    .align(Alignment.TopEnd),
                            )
                        }
                    }
                }

                EventsDetailLocation(
                    location = itemOrNull?.location ?: PLACEHOLDER,
                    modifier = Modifier.placeholder(isLoading),
                )

                if (itemOrNull?.cfpEnd != null) {
                    EventsDetailCfp(
                        cfpEnd = itemOrNull.cfpEnd,
                        cfpSite = itemOrNull.cfpSite,
                    )
                }
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
