package io.ashdavies.playground.github

import io.ashdavies.playground.github.GitHubRepository.Entry

internal interface GitHubRepository {

    val repository: dynamic

    interface Entry {

        val oid: String
        val text: String
    }
}

internal val GitHubRepository.entries: List<Entry>
    get() = repository
        .conferences
        .entries
        .unsafeCast<Array<dynamic>>()
        .map { it.data }
