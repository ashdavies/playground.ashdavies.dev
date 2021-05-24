package io.ashdavies.playground.conferences

import io.ashdavies.playground.github.GitHubRepository
import io.ashdavies.playground.github.GitHubService
import kotlin.js.json

internal fun GitHubRepository(
    vararg entries: GitHubRepository.Entry,
): GitHubRepository = object : GitHubRepository {
    override val repository: dynamic = json(
        "conferences" to json(
            "entries" to Array(entries.size) {
                json("data" to entries[it])
            }
        )
    )
}

internal fun GitHubService(
    vararg entries: GitHubRepository.Entry,
): GitHubService = object : GitHubService {
    override suspend fun getRepository(): GitHubRepository {
        return GitHubRepository(*entries)
    }
}