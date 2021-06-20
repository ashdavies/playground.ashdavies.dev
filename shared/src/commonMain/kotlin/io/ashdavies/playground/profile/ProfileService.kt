package io.ashdavies.playground.profile

import io.ashdavies.playground.database.Profile
import io.ashdavies.playground.network.Envelope
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val RANDOM_USER = "https://randomuser.me/api/"

interface ProfileService {
    suspend fun getProfile(): Profile
}

fun ProfileService(httpClient: HttpClient) = object : ProfileService {
    override suspend fun getProfile(): Profile = httpClient
        .get<Envelope<RandomUser>>(RANDOM_USER)
        .results
        .first()
        .toProfile()
}

private fun RandomUser.toProfile() = Profile(
    name = "${name.first} ${name.last}",
    location = "${location.city}, ${location.country}",
    picture = picture.large,
    id = login.uuid,
    position = null,
)