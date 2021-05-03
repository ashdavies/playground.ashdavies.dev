package io.ashdavies.playground.github

internal val GitHubQuery = """
query Conferences {
  repository(owner: "AndroidStudyGroup", name: "conferences") {
    conferences: object(expression: "HEAD:_conferences") {
      ... on Tree {
        entries {
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