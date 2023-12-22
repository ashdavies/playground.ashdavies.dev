package io.ashdavies.identity

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import io.ashdavies.content.PlatformContext

internal actual val PlatformContext.credentialDataStore: DataStore<Credential> by dataStore(
    serializer = ProtoSerializer(Credential.ADAPTER) { Credential() },
    fileName = "credential.pb",
)
