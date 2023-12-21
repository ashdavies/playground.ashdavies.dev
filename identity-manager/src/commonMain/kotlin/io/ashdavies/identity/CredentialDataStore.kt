package io.ashdavies.identity

import androidx.datastore.core.DataStore
import io.ashdavies.content.PlatformContext

public expect val PlatformContext.credentialDataStore: DataStore<Credential>
