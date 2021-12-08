package io.ashdavies.notion

expect class AuthServer(host: String, path: String, port: Int) {
    suspend fun awaitRedirectUri(): String
}
