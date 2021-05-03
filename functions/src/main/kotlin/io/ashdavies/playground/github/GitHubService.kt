package io.ashdavies.playground.github

import io.ashdavies.playground.graphql.GraphQl
import io.ashdavies.playground.graphql.graphql
import io.ashdavies.playground.humps.Humps
import io.ashdavies.playground.yaml.Yaml
import kotlinx.coroutines.await

internal class GitHubService(private val token: String) {

    suspend fun conferences(): Map<String, dynamic> {
        fun conference(value: String): dynamic {
            val yaml: dynamic = Yaml
                .parseAllDocuments(value)
                .first()
                .toJSON()

            return Humps.camelizeKeys(yaml)
        }

        val entries: List<GitHubRepository.Entry> = GraphQl
            .graphql<GitHubRepository>(GitHubQuery, token = token)
            .await()
            .entries

        return entries.associate { it.oid to conference(it.text) }
    }
}

