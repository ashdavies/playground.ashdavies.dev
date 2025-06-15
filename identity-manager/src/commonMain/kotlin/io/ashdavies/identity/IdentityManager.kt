package io.ashdavies.identity

import io.ashdavies.content.PlatformContext
import io.ashdavies.delegates.notNull
import io.ashdavies.sql.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge

public interface IdentityManager {
    public val state: Flow<IdentityState>
    public suspend fun signIn()
}

public fun IdentityManager(
    platformContext: PlatformContext,
    credentialQueries: CredentialQueries,
): IdentityManager = IdentityManager(
    credentialQueries = credentialQueries,
    identityService = GoogleIdIdentityService(
        context = platformContext,
    ),
)

internal fun IdentityManager(
    credentialQueries: CredentialQueries,
    identityService: GoogleIdIdentityService,
): IdentityManager = object : IdentityManager {

    private val states = MutableStateFlow<IdentityState>(IdentityState.Unauthenticated)

    private val queries = credentialQueries.selectAll().mapToOneOrNull(Dispatchers.Default) {
        if (it != null) IdentityState.Authenticated(it.profilePictureUrl) else IdentityState.Unauthenticated
    }

    override val state: Flow<IdentityState> = merge(states, queries)

    override suspend fun signIn() {
        val serverClientId by notNull { BuildConfig.SERVER_CLIENT_ID }
        val identityRequest = GoogleIdIdentityRequest(serverClientId)
        val identityResponse = try {
            identityService.request(identityRequest)
        } catch (exception: UnsupportedOperationException) {
            states.value = IdentityState.Failure(exception.message)
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
