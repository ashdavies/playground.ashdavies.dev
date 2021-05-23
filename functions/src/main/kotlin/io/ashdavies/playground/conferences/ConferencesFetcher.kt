package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.github.GitHubRepository
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.humps.Humps
import io.ashdavies.playground.store.Fetcher
import io.ashdavies.playground.yaml.Yaml
import io.ashdavies.playground.github.entries

internal suspend fun ConferencesFetcher(service: GitHubService): ConferencesFetcher = Fetcher {

    fun conference(entry: GitHubRepository.Entry): Conference {
        val yaml: dynamic = Yaml
            .parseAllDocuments(entry.text)
            .first()
            .toJSON()

        yaml.id = entry.oid
        return Humps.camelizeKeys(yaml)
    }

    service
        .getRepository()
        .entries
        .map(::conference)
}

internal typealias ConferencesFetcher = Fetcher<Unit, List<Conference>>
