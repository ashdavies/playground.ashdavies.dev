package io.ashdavies.playground.github

import com.apollographql.apollo.ApolloClient
import io.ashdavies.github.EventsQuery
import io.ashdavies.playground.aggregator.ApolloClient
import io.ashdavies.playground.apollo.asBlobs
import io.ashdavies.playground.apollo.entries
import io.ashdavies.playground.apollo.requireOid
import io.ashdavies.playground.apollo.requireText
import io.ashdavies.yaml.Yaml
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public interface GitHubService {

    public suspend fun <T : Any> getEvents(mapper: Mapper<T>): List<T>

    public fun interface Mapper<T : Any> {

        @Suppress("LongParameterList")
        public operator fun invoke(
            id: String,
            name: String,
            website: String,
            location: String,
            status: String?,
            online: Boolean?,
            dateStart: String,
            dateEnd: String,
            cfpStart: String?,
            cfpEnd: String?,
            cfpSite: String?,
        ): T
    }

    @OptIn(ExperimentalSerializationApi::class)
    public companion object Default : GitHubService by GitHubService(
        client = ApolloClient(),
        yaml = Yaml,
    )
}

@ExperimentalSerializationApi
public fun GitHubService(client: ApolloClient, yaml: Yaml): GitHubService = object : GitHubService {
    override suspend fun <T : Any> getEvents(mapper: GitHubService.Mapper<T>): List<T> {
        val entries: List<EventsQuery.OnBlob> = client
            .query(EventsQuery())
            .execute()
            .entries
            .asBlobs()

        val serializer = GitHubConference.serializer()

        return entries.map { blob ->
            val value = yaml.decodeFromString(serializer, blob.requireText())

            mapper(
                id = blob.requireOid(), name = value.name, website = value.website,
                location = value.location, status = value.status, online = value.online,
                dateStart = value.dateStart, dateEnd = value.dateEnd, cfpStart = value.cfp?.start,
                cfpEnd = value.cfp?.end, cfpSite = value.cfp?.site,
            )
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
}
