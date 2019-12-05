package io.ashdavies.playground.conferences

import com.google.firebase.firestore.Query
import io.ashdavies.playground.extensions.toObjects
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.tasks.await

internal class ConferencesService(private val query: Query) {

  suspend fun conferences(page: Int, limit: Long): List<Conference> = query
      .limit(limit)
      .get()
      .await()
      .toObjects()
}