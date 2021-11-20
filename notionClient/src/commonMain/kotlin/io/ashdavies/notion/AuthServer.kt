package io.ashdavies.notion

expect class AuthServer(config: AuthServerConfig) {
    suspend fun await(): AuthResult
    suspend fun close()
}

data class AuthServerConfig(
    val host: String,
    val path: String,
    val port: Int,
)

sealed class AuthResult {
    data class Success(
        val code: String,
        val state: String?,
    ) : AuthResult()

    data class Error(
        val error: String,
        val state: String?,
    ) : AuthResult()
}
