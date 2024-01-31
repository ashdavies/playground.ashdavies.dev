package io.ashdavies.identity

import io.ashdavies.sql.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

public interface IdentityManager {
    public val state: Flow<IdentityState>
    public suspend fun signIn()
}

internal fun IdentityManager(
    credentialQueries: CredentialQueries,
    identityService: GoogleIdIdentityService,
): IdentityManager = object : IdentityManager {

    override val state: Flow<IdentityState> = credentialQueries.selectAll().mapToOneOrNull {
        if (it != null) IdentityState.Authenticated(it.profilePictureUrl) else IdentityState.Unauthenticated
    }

    override suspend fun signIn() {
        val identityRequest = GoogleIdIdentityRequest(BuildConfig.SERVER_CLIENT_ID)
        val identityResponse = try {
            identityService.request(identityRequest)
        } catch (_: UnsupportedOperationException) {
            return
        }

        credentialQueries.insertOrReplace(
            credential = Credential(
                uuid = identityResponse.uuid,
                profilePictureUrl = identityResponse.pictureProfileUrl,
            ),
        )
    }
}
