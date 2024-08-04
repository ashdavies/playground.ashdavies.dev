package io.ashdavies.aggregator

import com.apollographql.apollo.ApolloClient
import io.ashdavies.github.EventsQuery
import io.ashdavies.playground.aggregator.BuildConfig
import io.ashdavies.yaml.Yaml
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString

public interface GitHubService {

    public suspend operator fun <T : Any> invoke(transform: (String, AsgEvent) -> T): List<T>

    @OptIn(ExperimentalSerializationApi::class)
    public companion object Default : GitHubService by GitHubService(
        client = ApolloClient(BuildConfig.GITHUB_GRAPHQL_SERVER_URL),
        yaml = Yaml,
    )
}

@ExperimentalSerializationApi
public fun GitHubService(client: ApolloClient, yaml: Yaml): GitHubService = object : GitHubService {
    override suspend operator fun <T : Any> invoke(transform: (String, AsgEvent) -> T): List<T> {
        val response = client
            .query(EventsQuery())
            .execute()

        return response.dataAssertNoErrors.repository.events.onTreeOrThrow.entries.map {
            with(it.contents.onBlobOrThrow) {
                transform(oid as String, yaml.decodeFromString(text))
            }
        }
    }
}

private val EventsQuery.Events.onTreeOrThrow: EventsQuery.OnTree
    get() = onTree ?: error("Failed to represent $this as a tree")

private val EventsQuery.Contents.onBlobOrThrow: EventsQuery.OnBlob
    get() = onBlob ?: error("Failed to represent $this as a blob")
