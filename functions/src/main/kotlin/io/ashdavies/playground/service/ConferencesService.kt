package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.DocumentData
import io.ashdavies.playground.firebase.QueryDocumentSnapshot
import io.ashdavies.playground.firebase.delete
import io.ashdavies.playground.firebase.set
import io.ashdavies.playground.github.GitHubService
import kotlinx.coroutines.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic

private const val CONFERENCES = "conferences"

@OptIn(ExperimentalSerializationApi::class)
internal val ConferencesService: (Request, Response<dynamic>) -> Unit =
    coroutineService { req, res ->
        val conferencesServiceArgs =
            Json.decodeFromDynamic(ConferencesServiceArgs.serializer(), req.query)

        val collection: CollectionReference = Admin
            .firestore()
            .collection(CONFERENCES)

        if (conferencesServiceArgs.refresh) {
            val gitHubService = GitHubService(conferencesServiceArgs.token)
            val remoteConferences = gitHubService.getRemoteConferences()
            val localConferences = collection.getLocalConferences()

            for ((oid, conference) in remoteConferences) {
                when (oid) {
                    in localConferences -> localConferences.remove(oid)
                    else -> {
                        collection.set(oid, conference)
                    }
                }
            }

            for ((oid, _) in localConferences) {
                collection.delete(oid)
            }
        }

        val localConferences: List<DocumentData> = collection
            .getLocalConferences()
            .map { it.value.data() }

        res.send(localConferences)
    }

private suspend fun CollectionReference.getLocalConferences(): MutableMap<String, QueryDocumentSnapshot> =
    get()
        .await()
        .docs
        .associateBy { it.id }
        .toMutableMap()

private suspend fun GitHubService.getRemoteConferences(): MutableMap<String, dynamic> =
    conferences()
        .toMutableMap()

@Serializable
private data class ConferencesServiceArgs(
    val refresh: Boolean = false,
    val limit: Int = 10,
    val token: String,
)

