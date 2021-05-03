package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.firebase.*
import io.ashdavies.playground.github.GitHubService
import kotlinx.coroutines.await
import kotlin.js.JSON.parse
import kotlin.js.JSON.stringify

internal val ConferencesService: (Request, Response<dynamic>) -> Unit =
    coroutineService { req, res ->
        val gitHubService = GitHubService(req.query.token as String)
        val collection: CollectionReference<DocumentData> = Admin
            .firestore()
            .collection("conferences")

        val remoteConferences = gitHubService.getRemoteConferences()
        val localConferences = collection.getLocalConferences()
        var localAdded = 0

        for ((oid, conference) in remoteConferences) {
            when (oid) {
                in localConferences -> localConferences.remove(oid)
                else -> {
                    collection.set(oid, parse(stringify(conference)))
                    localAdded++
                }
            }
        }

        for ((oid, _) in localConferences) {
            collection.delete(oid)
        }

        res.send("$localAdded conference records added")
    }

private suspend fun CollectionReference<DocumentData>.getLocalConferences(): MutableMap<String, QueryDocumentSnapshot<DocumentData>> =
    get()
        .await()
        .docs
        .associateBy { it.id }
        .toMutableMap()

private suspend fun CollectionReference<DocumentData>.delete(oid: String): WriteResult =
    doc(oid)
        .delete()
        .await()

private suspend fun CollectionReference<DocumentData>.set(
    oid: String,
    conference: DocumentData,
): WriteResult = doc(oid)
    .set(conference)
    .await()

private suspend fun GitHubService.getRemoteConferences(): MutableMap<String, dynamic> =
    conferences()
        .toMutableMap()