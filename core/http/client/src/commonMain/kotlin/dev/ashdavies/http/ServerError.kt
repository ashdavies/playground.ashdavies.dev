package dev.ashdavies.http

import kotlinx.serialization.Serializable

@Serializable
public data class ServerError(
    override val message: String,
    val code: Int,
) : Throwable()
