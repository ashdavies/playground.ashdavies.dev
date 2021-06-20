package io.ashdavies.playground.profile

import io.ashdavies.playground.database.Profile
import io.ashdavies.playground.network.Envelope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf

private const val RANDOM_USER = "https://randomuser.me/api/"

interface ProfileService {
    suspend fun getProfile(): Profile
}

fun ProfileService(httpClient: HttpClient) = object : ProfileService {
    override suspend fun getProfile(): Profile {
        try {
            val json = Json {
                ignoreUnknownKeys = true
                serializersModule = serializersModuleOf(
                    serializer = Envelope.serializer(RandomUser.serializer())
                )
            }

            val responseAsString = httpClient.get<String>(RANDOM_USER)
            println("responseAsString = $responseAsString")

            val envelopeAsRandomUserSerializer = Envelope.serializer(RandomUser.serializer())
            println("envelopeAsRandomUser = ${json.decodeFromString(envelopeAsRandomUserSerializer, responseAsString)}")

            return httpClient
                .get<Envelope<RandomUser>>(RANDOM_USER)
                .results
                .first()
                .toProfile()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }
}

private fun RandomUser.toProfile() = Profile(
    name = "${name.first} ${name.last}",
    location = "$city, $country",
    picture = picture.large,
    id = login.uuid,
    position = null,
)