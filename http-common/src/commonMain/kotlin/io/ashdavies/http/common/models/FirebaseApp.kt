package io.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class FirebaseApp(
    public val appId: String,
)
