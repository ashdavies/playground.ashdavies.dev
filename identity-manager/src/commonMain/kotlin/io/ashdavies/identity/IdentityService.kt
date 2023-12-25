package io.ashdavies.identity

public fun interface IdentityService<T : IdentityRequest> {
    public suspend fun request(request: T): IdentityResponse
}

public sealed interface IdentityRequest

public data class IdentityResponse(
    val uuid: String,
    val pictureProfileUrl: String?,
)
