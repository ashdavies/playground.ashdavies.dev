package io.ashdavies.identity

public sealed interface IdentityState {
    public data class Authenticated(val pictureProfileUrl: String?) : IdentityState
    public data class Failure(val message: String?) : IdentityState

    public data object Unauthenticated : IdentityState
    public data object Unsupported : IdentityState
}
