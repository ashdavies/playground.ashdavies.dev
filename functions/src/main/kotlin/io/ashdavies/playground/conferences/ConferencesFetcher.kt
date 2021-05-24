package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.github.GitHubRepository
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.github.entries
import io.ashdavies.playground.store.Fetcher
import io.ashdavies.playground.yaml.Yaml

internal suspend fun ConferencesFetcher(service: GitHubService): ConferencesFetcher = Fetcher {
    service
        .getRepository()
        .entries
        .map(::parse)
}

private fun parse(entry: GitHubRepository.Entry): Conference {
    return Yaml
        .decodeFromString(ConferenceYaml.serializer(), entry.text)
        .toConference(entry.oid)
}

internal typealias ConferencesFetcher = Fetcher<Unit, List<Conference>>
