package dev.ashdavies.playground.event.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.valentinilk.shimmer.shimmer
import dev.ashdavies.identity.IdentityState
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.playground.event.daysUntilCfpEnd
import dev.ashdavies.playground.event.formatDateRange
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.uniform
import dev.ashdavies.playground.ui.BackButton
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.ProfileActionButton
import dev.ashdavies.playground.ui.emptyString
import dev.ashdavies.playground.ui.gradientBrush
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.cfp_closed
import dev.ashdavies.playground.ui.resources.cfp_open
import dev.ashdavies.playground.ui.resources.event_date
import dev.ashdavies.playground.ui.resources.event_location
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

private val Event.thumbnailImageSeed: Long
    get() = "$name$location"
        .hashCode()
        .toLong()

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(EventScreen.Detail::class, AppScope::class)
public fun EventDetailUi(state: EventDetailState, modifier: Modifier = Modifier) {
    val itemOrNull = (state.itemState as? EventDetailState.ItemState.Done)?.item
    val isLoading = state.itemState is EventDetailState.ItemState.Loading
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
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
                .padding(horizontal = MaterialTheme.spacing.large.horizontal)
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium.vertical),
        ) {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                EventsDetailImage(
                    imageUrl = itemOrNull?.attrs?.imageUrl,
                    seed = itemOrNull?.thumbnailImageSeed ?: 0L,
                )
            }

            EventsDetailRow(
                icon = Icons.Outlined.MyLocation,
                label = stringResource(Res.string.event_location),
                text = itemOrNull?.location ?: emptyString(),
            )

            if (itemOrNull != null) {
                EventsDetailRow(
                    icon = Icons.Outlined.Event,
                    label = stringResource(Res.string.event_date),
                    text = formatDateRange(itemOrNull.dateStart, itemOrNull.dateEnd),
                )
            }

            itemOrNull?.attrs?.cfpEnd?.let { cfpEnd ->
                EventsDetailCfp(
                    cfpSite = itemOrNull.attrs.cfpSite,
                    cfpStart = itemOrNull.attrs.cfpStart?.let(LocalDate::parse),
                    cfpEnd = LocalDate.parse(cfpEnd),
                )
            }
        }
    }
}

@Composable
private fun EventsDetailImage(
    imageUrl: String?,
    seed: Long,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .background(gradientBrush(seed))
            .fillMaxWidth()
            .height(200.dp),
        placeholder = null,
    )
}

@Composable
private fun EventsDetailRow(
    icon: ImageVector,
    label: String,
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
) {
    EventsDetailCard(modifier, onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )

        Column(Modifier.weight(1f)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun EventsDetailCfp(
    cfpSite: String?,
    cfpStart: LocalDate?,
    cfpEnd: LocalDate,
    modifier: Modifier = Modifier,
) {
    val isOpen = LocalInspectionMode.current || daysUntilCfpEnd(cfpEnd) > 0
    val uriHandler = LocalUriHandler.current

    EventsDetailRow(
        icon = Icons.Outlined.Campaign,
        label = stringResource(if (isOpen) Res.string.cfp_open else Res.string.cfp_closed),
        text = formatDateRange(cfpStart ?: cfpEnd, cfpEnd),
        modifier = modifier,
        onClick = if (isOpen && cfpSite != null) {
            { uriHandler.openUri(cfpSite) }
        } else {
            null
        },
        trailingIcon = if (isOpen && cfpSite != null) Icons.Outlined.Link else null,
    )
}

@Composable
private fun EventsDetailCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.large
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    val rowContent: @Composable () -> Unit = {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.medium.uniform),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium.horizontal),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }

    when (onClick) {
        null -> Card(modifier.fillMaxWidth(), shape, colors) { rowContent() }
        else -> Card(onClick, modifier.fillMaxWidth(), shape = shape, colors = colors) { rowContent() }
    }
}
