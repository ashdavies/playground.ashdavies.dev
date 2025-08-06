package dev.ashdavies.identity

import app.cash.sqldelight.coroutines.mapToOneOrNull
import dev.ashdavies.sql.Suspended
import dev.ashdavies.sql.mapAsFlow
import io.ashdavies.delegates.notNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.coroutines.CoroutineContext

public interface IdentityManager {
    public val state: Flow<IdentityState>
    public suspend fun signIn()
}

public fun IdentityManager(
    credentialQueries: Suspended<CredentialQueries>,
    identityService: GoogleIdIdentityService,
    coroutineContext: CoroutineContext,
): IdentityManager = object : IdentityManager {

    private val states = MutableStateFlow<IdentityState>(IdentityState.Unauthenticated)

    private val queries = credentialQueries
        .mapAsFlow(coroutineContext) { it.selectAll() }
        .mapToOneOrNull(coroutineContext)
        .map(::IdentityState)

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

        credentialQueries().insertOrReplace(
            credential = Credential(
                uuid = identityResponse.uuid,
                profilePictureUrl = identityResponse.pictureProfileUrl,
            ),
        )
    }
}

private fun IdentityState(credential: Credential?): IdentityState = when (credential) {
    is Credential -> IdentityState.Authenticated(credential.profilePictureUrl)
    else -> IdentityState.Unauthenticated
}
