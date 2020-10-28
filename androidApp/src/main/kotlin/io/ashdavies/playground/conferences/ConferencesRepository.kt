package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.StoreRequest
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ConferencesRepository(
    private val conferencesStore: ConferencesStore,
) {

    fun getAll(): Flow<List<Conference>> =
        conferencesStore
            .stream(StoreRequest.cached(Unit, refresh = true))
            .map { it.dataOrNull() ?: emptyList() }
}
