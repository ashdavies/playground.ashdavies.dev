package io.ashdavies.identity

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private const val CREDENTIAL_FILE_NAME = "credential.pb"

private val PlatformContext.credentialDataStore by dataStore(
    serializer = ProtoSerializer(Credential.ADAPTER) { Credential() },
    fileName = CREDENTIAL_FILE_NAME,
)

public actual fun IdentityManager(context: PlatformContext): IdentityManager = object : IdentityManager {

    private val dataStore: DataStore<Credential> = context.credentialDataStore

    override val state: Flow<IdentityState> = flowOf(IdentityState.Unauthenticated)
}
