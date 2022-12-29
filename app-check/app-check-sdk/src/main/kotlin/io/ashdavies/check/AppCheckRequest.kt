package io.ashdavies.check

import kotlinx.serialization.Serializable

@Serializable
public data class AppCheckRequest(val appId: String)
