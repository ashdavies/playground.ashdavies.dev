package dev.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class DomainWrapper<T : Any>(
    val attrs: Map<String, String>,
    val value: T,
)
