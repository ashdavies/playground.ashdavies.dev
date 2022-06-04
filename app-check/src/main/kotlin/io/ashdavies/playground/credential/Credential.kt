package io.ashdavies.playground.credential

internal sealed interface Credential {
    data class ServiceAccountCredential(
        val serviceAccountId: String,
        val clientEmail: String,
    ) : Credential
}
