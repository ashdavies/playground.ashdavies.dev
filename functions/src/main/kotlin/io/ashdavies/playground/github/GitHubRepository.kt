package io.ashdavies.playground.github

import io.ashdavies.playground.github.GitHubRepository.NestedRepository.NestedConferences.NestedEntry.Entry

internal interface GitHubRepository {

    val repository: NestedRepository

    interface NestedRepository {

        val conferences: NestedConferences

        interface NestedConferences {

            val entries: Array<NestedEntry>

            interface NestedEntry {

                val data: Entry

                interface Entry {

                    val oid: String
                    val text: String
                }
            }
        }
    }
}

internal val GitHubRepository.entries: List<Entry>
    get() = repository
        .conferences
        .entries
        .map { it.data }
