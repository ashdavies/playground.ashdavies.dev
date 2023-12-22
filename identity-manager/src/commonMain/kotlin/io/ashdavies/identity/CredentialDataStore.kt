package io.ashdavies.identity

import androidx.datastore.core.DataStore
import io.ashdavies.content.PlatformContext

internal expect val PlatformContext.credentialDataStore: DataStore<Credential>
