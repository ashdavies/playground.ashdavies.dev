package io.ashdavies.playground.profile

import io.ashdavies.playground.database.Profile
import io.ashdavies.playground.network.Envelope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

private const val RANDOM_USER = "https://randomuser.me/api/"

private val MaxMustermann = Profile(
    id = "1234-5678-9123-4567",
    name = "Max Mustermann",
    location = null,
    position = null,
    picture = null,
)

interface ProfileService {
    suspend fun authenticate(): Boolean
    suspend fun getProfile(): Flow<Profile?>
}

fun ProfileService(httpClient: HttpClient) = object : ProfileService {
    override suspend fun authenticate(): Boolean = false

    override suspend fun getProfile(): Flow<Profile?> = httpClient
            .get<Envelope<RandomUser>>(RANDOM_USER)
            .results
            .first()
            .toProfile()
    }
}

private fun RandomUser.toProfile() = Profile(
    name = "${name.first} ${name.last}",
    location = "${location.city}, ${location.country}",
    position = login.username,
    picture = picture.large,
    id = login.uuid,
)
