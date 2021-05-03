package io.ashdavies.playground.github

import io.ashdavies.playground.graphql.GraphQl
import io.ashdavies.playground.graphql.graphql
import io.ashdavies.playground.humps.Humps
import io.ashdavies.playground.yaml.Yaml
import kotlinx.coroutines.await
import kotlin.js.Date

private val GraphQlQuery = """
query Conferences {
  repository(owner: "AndroidStudyGroup", name: "conferences") {
    conferences: object(expression: "HEAD:_conferences") {
      ... on Tree {
        oid
        entries {
          oid
          name
          data: object {
            ... on Blob {
              oid
              text
            }
          }
        }
      }
    }
  }
}
""".trimMargin()

internal class GitHubService(private val token: String) {

    suspend fun conferences(): List<Any?> {
        fun conference(value: BlobEnvelope): Conference {
            val yaml: dynamic = Yaml
                .parseAllDocuments(value.data.text)[0]
                .toJSON()

            return Humps
                .camelizeKeys(yaml)
                .unsafeCast<Conference>()
        }

        val envelope: RepositoryEnvelope = GraphQl
            .graphql<RepositoryEnvelope>(GraphQlQuery, token = token)
            .await()

        val entries: Array<BlobEnvelope> = envelope
            .repository
            .conferences
            .entries

        return entries.map(::conference)
    }
}

internal data class Conference(
    val name: String,
    val location: String,
    val website: String,
    val dateStart: String,
    val dateEnd: String,
    val online: Boolean,
    val status: String,
    val cfp: Cfp,
)

internal data class Cfp(
    val start: Date,
    val end: Date,
    val site: String,
)

internal interface RepositoryEnvelope {
    val repository: Repository
}

internal interface Repository {
    val conferences: ConferencesEnvelope
}

internal interface ConferencesEnvelope {
    val entries: Array<BlobEnvelope>
}

internal interface BlobEnvelope {
    val data: Blob
}

internal interface Blob {
    val text: String
}