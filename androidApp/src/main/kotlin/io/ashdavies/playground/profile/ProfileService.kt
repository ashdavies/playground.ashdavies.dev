package io.ashdavies.playground.profile

import io.ashdavies.playground.database.Profile
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

private const val RANDOM_USER = "https://randomuser.me/api/"

internal interface ProfileService {
    suspend fun getProfile(): Profile
}

internal fun ProfileService(httpClient: HttpClient) = object : ProfileService {
    override suspend fun getProfile(): Profile {
        val randomUser: RandomUser = httpClient
            .get<Envelope<RandomUser>>(RANDOM_USER)
            .results
            .first()

        return with(randomUser) {
            Profile(
                id = login.uuid,
                name = "${name.first} ${name.last}",
                location = "$city, $country",
                picture = picture.large,
                position = null,
            )
        }
    }
}

@Serializable
private data class Envelope<T>(
    val results: List<T>,
)

@Serializable
private data class RandomUser(
    val name: Name,
    val city: String,
    val country: String,
    val login: Login,
    val picture: Picture,
) {

    @Serializable
    data class Name(
        val first: String,
        val last: String,
    )

    @Serializable
    data class Login(
        val uuid: String,
    )

    @Serializable
    data class Picture(
        val large: String,
    )
}