package dev.ashdavies.playground.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.valentinilk.shimmer.shimmer
import dev.ashdavies.playground.material.applyIf
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.sizing
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.uniform
import dev.ashdavies.playground.material.values
import dev.ashdavies.playground.ui.CenterAlignedTopAppBar
import dev.ashdavies.playground.ui.ErrorLayout
import dev.ashdavies.playground.ui.emptyString
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.cfp_closed
import dev.ashdavies.playground.ui.resources.common_search
import dev.ashdavies.playground.ui.resources.conference_attendees
import dev.ashdavies.playground.ui.resources.conference_tracks
import dev.ashdavies.playground.ui.resources.sort_by_date
import dev.ashdavies.playground.ui.resources.upcoming_events
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Inject
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(EventScreen.List::class, AppScope::class)
public fun EventListUi(state: EventListState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { CenterAlignedTopAppBar(stringResource(Res.string.upcoming_events)) },
    ) { contentPadding ->
        when (state) {
            is EventListState.Success -> EventListContent(
                state = state,
                modifier = Modifier.padding(contentPadding),
            )

            is EventListState.Failure -> ErrorLayout(
                message = state.message,
                icon = state.icon,
            )
        }
    }
}

@Composable
private fun EventListContent(
    state: EventListState.Success,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        /*SearchBar(
            results = state.searchResults,
            onSearch = { state.eventSink(EventListState.Success.Event.Search(it)) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )*/

        SortingButton(
            sorting = state.sorting,
            onClick = { state.eventSink(EventListState.Success.Event.ToggleSorting) },
            modifier = Modifier.padding(MaterialTheme.spacing.medium.horizontal),
        )

        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = { state.eventSink(EventListState.Success.Event.Refresh) },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = MaterialTheme.spacing.large.values,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large.vertical),
            ) {
                itemsIndexed(state.itemList) { index, item ->
                    EventListItemContent(
                        item = item,
                        isRefreshing = state.isRefreshing,
                        onClick = { state.eventSink(EventListState.Success.Event.ItemClick(index)) },
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchBar(
    results: ImmutableList<String>,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val textFieldState = remember { TextFieldState() }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                onSearch = {
                    onSearch(it)
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(stringResource(Res.string.common_search)) },
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            results.forEach { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier
                        .clickable {
                            textFieldState.edit { replace(0, length, result) }
                            expanded = false
                        }
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SortingButton(
    sorting: EventListState.Success.Sorting,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxWidth()) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Text(stringResource(Res.string.sort_by_date))

            Icon(
                imageVector = when (sorting) {
                    EventListState.Success.Sorting.DESCENDING -> Icons.Outlined.ArrowDownward
                    EventListState.Success.Sorting.ASCENDING -> Icons.Outlined.ArrowUpward
                },
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun EventListItemContent(
    item: EventListState.Success.Item?,
    isRefreshing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .applyIf(isRefreshing) { it.shimmer() }
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large.uniform)
                .fillMaxWidth(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.vertical)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Badge {
                        Text(stringResource(Res.string.cfp_closed))
                    }

                    VerticalDivider(Modifier.height(1.dp))

                    item?.trackCount?.let { trackCount ->
                        Text(pluralStringResource(Res.plurals.conference_tracks, trackCount, trackCount))
                    }

                    VerticalDivider(Modifier.height(1.dp))

                    Text(item?.series ?: emptyString())
                }

                Text(
                    text = item?.name ?: emptyString(),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                    )

                    Text(
                        text = item?.location ?: emptyString(),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }

                Spacer(Modifier.height(MaterialTheme.spacing.medium.vertical))

                Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small.horizontal)) {
                    item?.tagList?.forEach {
                        Badge { Text(it) }
                    }

                    item?.attendeeCount?.let { attendeeCount ->
                        Badge {
                            Text(
                                pluralStringResource(
                                    Res.plurals.conference_attendees,
                                    attendeeCount,
                                    attendeeCount,
                                ),
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .applyIf(isRefreshing) { it.shimmer() }
                    .align(Alignment.TopEnd),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .padding(MaterialTheme.spacing.small),
                    verticalArrangement = Arrangement.aligned(Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = item?.dateSubtitle ?: emptyString(),
                        style = MaterialTheme.typography.labelSmall,
                    )

                    Text(
                        text = item?.dateTitle ?: emptyString(),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkAdd,
                    contentDescription = null,
                )
            }
        }
    }
}
