package io.ashdavies.playground.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.kotlin.mapToOne
import io.ashdavies.playground.rememberPlaygroundDatabase
import kotlinx.coroutines.flow.Flow

internal fun interface DetailsRepository {
    fun getEvent(id: String): Flow<Event>
}

internal fun DetailsRepository(eventsQueries: EventsQueries) = DetailsRepository { id ->
    eventsQueries.selectById(id).mapToOne()
}

@Composable
@OptIn(MultipleReferenceWarning::class)
internal fun rememberDetailsRepository(
    eventsQueries: EventsQueries = rememberPlaygroundDatabase().eventsQueries,
) = remember(eventsQueries) { DetailsRepository(eventsQueries) }
