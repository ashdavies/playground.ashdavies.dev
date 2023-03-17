package io.ashdavies.playground.github

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import io.ashdavies.playground.aggregator.ApolloClient
import io.ashdavies.playground.apollo.asBlobs
import io.ashdavies.playground.apollo.entries
import io.ashdavies.playground.apollo.requireOid
import io.ashdavies.playground.apollo.requireText
import io.ashdavies.yaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public interface GitHubService {
    public suspend fun <T : Any> getEvents(
        mapper: (
            @ParameterName("id")
            String,
            @ParameterName("name")
            String,
            @ParameterName("website")
            String,
            @ParameterName("location")
            String,
            @ParameterName("status")
            String?,
            @ParameterName("online")
            Boolean?,
            @ParameterName("dateStart")
            String,
            @ParameterName("dateEnd")
            String,
            @ParameterName("cfpStart")
            String?,
            @ParameterName("cfpEnd")
            String?,
            @ParameterName("cfpSite")
            String?,
        ) -> T,
    ): List<T>

    public companion object Default : GitHubService by GitHubService(
        client = ApolloClient(),
        yaml = Yaml,
    )
}

@Suppress("NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")
public fun GitHubService(client: ApolloClient, yaml: Yaml) = object : GitHubService {
    override suspend fun <T : Any> getEvents(
        mapper: (
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
        ) -> T,
    ): List<T> {
        val entries: List<EventsQuery.AsBlob> = client
            .query(EventsQuery())
            .await()
            .entries
            .asBlobs()

        return entries.map {
            with(yaml.decodeFromString(GitHubConference.serializer(), it.requireText())) {
                mapper(
                    /* id */ it.requireOid(),
                    /* name */ name,
                    /* website */ website,
                    /* location */ location,
                    /* status */ status,
                    /* online */ online,
                    /* dateStart */ dateStart,
                    /* dateEnd */ dateEnd,
                    /* cfpStart */ cfp?.start,
                    /* cfpEnd */ cfp?.end,
                    /* cfpSite */ cfp?.site,
                )
            }
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
