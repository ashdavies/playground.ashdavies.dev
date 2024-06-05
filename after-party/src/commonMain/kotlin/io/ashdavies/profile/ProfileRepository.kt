package io.ashdavies.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.random.Profile
import io.ashdavies.random.RandomProvider
import io.ashdavies.random.getRandomUser
import io.ashdavies.sql.mapToOneOrNull
import io.ashdavies.sql.rememberLocalQueries
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
internal fun rememberProfileRepository(
    profileQueries: ProfileQueries = rememberLocalQueries { it.profileQueries },
    httpClient: HttpClient = LocalHttpClient.current,
): ProfileRepository = remember(httpClient, profileQueries) {
    ProfileRepository(
        randomProvider = RandomProvider(httpClient),
        profileQueries = profileQueries,
    )
}
