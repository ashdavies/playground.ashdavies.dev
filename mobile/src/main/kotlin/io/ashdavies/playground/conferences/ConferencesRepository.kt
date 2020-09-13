package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class ConferencesRepository(
    private val conferencesClient: ConferencesClient,
    private val conferencesDao: ConferencesDao,
    private val conferencesStore: ConferencesStore,
) {

    fun getAll(): Flow<List<Conference>> = flow {
        val result: List<Conference> = conferencesClient.getAll()
        conferencesDao.insert(result)
        emit(result)
    }
}
