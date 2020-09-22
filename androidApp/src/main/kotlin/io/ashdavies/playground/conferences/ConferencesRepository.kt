package io.ashdavies.playground.conferences

import io.ashdavies.playground.ktx.insertOrReplaceAll
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class ConferencesRepository(
    private val conferencesClient: ConferencesClient,
    private val conferenceQueries: ConferencesQueries,
    private val conferencesStore: ConferencesStore,
) {

    fun getAll(): Flow<List<Conference>> = flow {
        val result: List<Conference> = conferencesClient.getAll()
        conferenceQueries.insertOrReplaceAll(result)
        emit(result)
    }
}
