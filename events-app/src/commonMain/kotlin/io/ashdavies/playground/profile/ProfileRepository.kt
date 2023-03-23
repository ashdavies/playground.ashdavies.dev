package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.Profile
import io.ashdavies.playground.ProfileQueries
import io.ashdavies.playground.kotlin.mapToOneOrNull
import io.ashdavies.playground.rememberPlaygroundDatabase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.serialization.Serializable

private const val RANDOM_USER = "https://randomuser.me/api/"

internal fun interface ProfileRepository {
    fun getProfile(generateRandomIfEmpty: Boolean): Flow<Profile>
}

internal fun ProfileRepository(
    profileQueries: ProfileQueries,
    httpClient: HttpClient,
) = ProfileRepository { generateRandomIfEmpty ->
    profileQueries.selectAll().mapToOneOrNull {
        it ?: if (generateRandomIfEmpty) {
            Profile(getRandomUser(httpClient))
        } else {
            null
        }
    }.filterNotNull()
}

private suspend fun getRandomUser(httpClient: HttpClient): RandomUser = httpClient
    .get(RANDOM_USER)
    .body<Envelope<RandomUser>>()
    .results
    .first()

@Serializable
private data class Envelope<T>(
    val results: List<T>,
)

@Composable
@OptIn(MultipleReferenceWarning::class)
internal fun rememberProfileRepository(
    profileQueries: ProfileQueries = rememberPlaygroundDatabase().profileQueries,
    httpClient: HttpClient = LocalHttpClient.current,
): ProfileRepository = remember(httpClient, profileQueries) {
    ProfileRepository(profileQueries, httpClient)
}
