package io.ashdavies.identity

import io.ashdavies.content.randomUuid
import io.ashdavies.playground.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

public interface IdentityManager {
    public val state: Flow<IdentityState>
    public suspend fun signIn()
}

public fun IdentityManager(credentials: CredentialQueries): IdentityManager = object : IdentityManager {

    override val state: Flow<IdentityState> = credentials.selectAll().mapToOneOrNull {
        if (it != null) IdentityState.Authenticated(it.profile_picture_url) else IdentityState.Unauthenticated
    }

    override suspend fun signIn() {
        credentials.insertOrReplace(
            credential = Credential(
                uuid = randomUuid(),
                profile_picture_url = randomPhotoUrl(),
            ),
        )
    }
}

public fun randomPhotoUrl(size: Int = 200): String {
    return "https://picsum.photos/$size"
}
