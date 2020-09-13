package io.ashdavies.playground.conferences

import io.ashdavies.playground.github.GitHub
import io.ashdavies.playground.network.Conference
import retrofit2.http.GET
import retrofit2.http.Path

private const val ASG_CONFERENCES = "AndroidStudyGroup/conferences"

private const val CONFERENCES_API = "https://api.github.com/repos/$ASG_CONFERENCES/contents/_conferences"
private const val CONFERENCES_RAW = "https://raw.githubusercontent.com/$ASG_CONFERENCES/gh-pages/_conferences"

internal interface ConferencesService {

    @GET(CONFERENCES_API)
    suspend fun getAll(): List<GitHub.Item<Conference>>

    @GET("$CONFERENCES_API/{name}")
    suspend fun get(@Path("name") name: String): GitHub.Item<Conference>

    @GET("$CONFERENCES_RAW/{name}")
    suspend fun raw(@Path("name") name: String): Conference
}
