package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.GitHub

private const val ASG_CONFERENCES = "AndroidStudyGroup/conferences"

private const val CONFERENCES_API =
    "https://api.github.com/repos/$ASG_CONFERENCES/contents/_conferences"
private const val CONFERENCES_RAW =
    "https://raw.githubusercontent.com/$ASG_CONFERENCES/gh-pages/_conferences"

internal class ConferencesService {

    suspend fun getAll(): List<GitHub.Item<Conference>> {
        TODO("Not yet implemented $CONFERENCES_API")
    }

    suspend fun get(name: String): GitHub.Item<Conference> {
        TODO("Not yet implemented $CONFERENCES_API/{name}")
    }

    suspend fun raw(name: String): Conference {
        TODO("Not yet implemented $CONFERENCES_RAW/{name}")
    }
}
