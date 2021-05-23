package io.ashdavies.playground.github

import io.ashdavies.playground.graphql.GraphQl
import io.ashdavies.playground.graphql.graphql
import kotlinx.coroutines.await

internal interface GitHubService {

    suspend fun getRepository(): GitHubRepository
}

internal fun GitHubService(token: String): GitHubService =
    GitHubServiceImpl(token)

private class GitHubServiceImpl(private val token: String) : GitHubService {

    override suspend fun getRepository(): GitHubRepository =
        GraphQl
            .graphql<GitHubRepository>(GitHubQuery, token = token)
            .await()
}