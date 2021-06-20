package io.ashdavies.playground.events

import io.ashdavies.playground.database.Event
import io.ashdavies.playground.github.GitHubRepository
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.github.entries
import io.ashdavies.playground.store.Fetcher
import io.ashdavies.playground.yaml.Yaml

internal suspend fun EventsFetcher(service: GitHubService): EventsFetcher = Fetcher {
    service
        .getRepository()
        .entries
        .map(::parse)
}

private fun parse(entry: GitHubRepository.Entry): Event {
    return Yaml
        .decodeFromString(EventYaml.serializer(), entry.text)
        .toEvent(entry.oid)
}

internal typealias EventsFetcher = Fetcher<Unit, List<Event>>
