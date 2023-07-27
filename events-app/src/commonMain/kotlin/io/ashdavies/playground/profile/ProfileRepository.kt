package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.Profile
import io.ashdavies.playground.ProfileQueries
import io.ashdavies.playground.mapToOneOrNull
import io.ashdavies.playground.random.Profile
import io.ashdavies.playground.random.RandomProvider
import io.ashdavies.playground.random.getRandomUser
import io.ashdavies.playground.rememberPlaygroundDatabase
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal fun interface ProfileRepository {
    fun getProfile(generateRandomIfEmpty: Boolean): Flow<Profile>
}

internal fun ProfileRepository(
    profileQueries: ProfileQueries,
    randomProvider: RandomProvider,
) = ProfileRepository { generateRandomIfEmpty ->
    profileQueries.selectAll().mapToOneOrNull {
        it ?: if (generateRandomIfEmpty) Profile(randomProvider.getRandomUser()) else null
    }.filterNotNull()
}

@Composable
@OptIn(MultipleReferenceWarning::class)
internal fun rememberProfileRepository(
    profileQueries: ProfileQueries = rememberPlaygroundDatabase().profileQueries,
    httpClient: HttpClient = LocalHttpClient.current,
): ProfileRepository = remember(httpClient, profileQueries) {
    ProfileRepository(
        randomProvider = RandomProvider(httpClient),
        profileQueries = profileQueries,
    )
}
