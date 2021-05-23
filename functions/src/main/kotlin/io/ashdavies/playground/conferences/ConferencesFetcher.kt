package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.github.GitHubRepository
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.github.entries
import io.ashdavies.playground.store.Fetcher
import io.ashdavies.playground.yaml.Yaml

internal suspend fun ConferencesFetcher(service: GitHubService): ConferencesFetcher = Fetcher {
    val documents: List<GitHubRepository.Entry> = service
        .getRepository()
        .entries

    documents.map {
        Yaml
            .decodeFromString(ConferenceYaml.serializer(), it.text)
            .toConference(it.oid)
    }
}

internal typealias ConferencesFetcher = Fetcher<Unit, List<Conference>>
