package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.github.GitHubQuery
import io.ashdavies.playground.github.GitHubRepository
import io.ashdavies.playground.github.entries
import io.ashdavies.playground.graphql.GraphQl
import io.ashdavies.playground.graphql.graphql
import io.ashdavies.playground.humps.Humps
import io.ashdavies.playground.store.Fetcher
import io.ashdavies.playground.yaml.Yaml
import kotlinx.coroutines.await

internal suspend fun ConferencesFetcher(token: String): ConferencesFetcher = Fetcher {

    fun conference(entry: GitHubRepository.Entry): Conference {
        val yaml: dynamic = Yaml
            .parseAllDocuments(entry.text)
            .first()
            .toJSON()

        yaml.id = entry.oid
        return Humps.camelizeKeys(yaml)
    }

    GraphQl
        .graphql<GitHubRepository>(GitHubQuery, token = token)
        .await()
        .entries
        .map(::conference)
}

internal typealias ConferencesFetcher = Fetcher<Unit, List<Conference>>
