package io.ashdavies.playground.github

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import io.ashdavies.playground.apollo.asBlobs
import io.ashdavies.playground.apollo.entries
import io.ashdavies.playground.apollo.requireOid
import io.ashdavies.playground.apollo.requireText
import io.ashdavies.playground.events.Event
import io.ashdavies.playground.yaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface GitHubService {
    suspend fun getEvents(): List<Event>
}

internal fun GitHubService(client: ApolloClient, yaml: Yaml) = object : GitHubService {
    override suspend fun getEvents(): List<Event> {
        val entries: List<ConferencesQuery.AsBlob> = client
            .query(ConferencesQuery())
            .await()
            .entries
            .asBlobs()

        return entries.map {
            yaml
                .decodeFromString(GitHubConference.serializer(), it.requireText())
                .toEvent(it.requireOid())
        }
    }
}

@Serializable
private data class GitHubConference(
    @SerialName("name") val name: String,
    @SerialName("website") val website: String,
    @SerialName("location") val location: String,
    @SerialName("online") val online: Boolean? = false,
    @SerialName("status") val status: String? = null,
    @SerialName("date_start") val dateStart: String,
    @SerialName("date_end") val dateEnd: String,
    @SerialName("cfp") val cfp: Cfp? = null,
) {

    @Serializable
    data class Cfp(
        @SerialName("start") val start: String,
        @SerialName("end") val end: String,
        @SerialName("site") val site: String? = null,
    )

    fun toEvent(id: String) = Event(
        cfpSite = cfp?.site ?: website,
        cfpStart = cfp?.start,
        dateStart = dateStart,
        location = location,
        cfpEnd = cfp?.end,
        dateEnd = dateEnd,
        website = website,
        online = online,
        status = status,
        name = name,
        id = id,
    )
}