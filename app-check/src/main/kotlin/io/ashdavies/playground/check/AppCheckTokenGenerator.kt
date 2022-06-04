package io.ashdavies.playground.check

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap
import java.time.Instant.now
import java.util.Base64
import java.util.Base64.Encoder
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

private const val FIREBASE_APP_CHECK_AUDIENCE =
    "https://firebaseappcheck.googleapis.com/google.firebase.appcheck.v1beta.TokenExchangeService"

internal class AppCheckTokenGenerator(private val signer: CryptoSigner) {

    suspend fun createCustomToken(appId: String, options: AppCheckTokenOptions? = null): String {
        require(appId.isNotEmpty()) { throw InvalidArgumentError("`appId` must be a non-empty string") }

        val header = mapOf(
            "alg" to signer.algorithm,
            "typ" to "JWT",
        )

        val body = mapOf(
            "exp" to now().epochSecond + 1.hours.inWholeSeconds,
            "aud" to FIREBASE_APP_CHECK_AUDIENCE,
            "iss" to signer.accountId,
            "sub" to signer.accountId,
            "app_id" to appId,
        ) + (options?.encodeToMap() ?: emptyMap())

        val token = "${header.encodeToBase64()}.${body.encodeToBase64()}"
        val signature = signer.sign(token)

        return "${token}.${signature.encodeToBase64()}"
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun AppCheckTokenOptions.encodeToMap(): Map<String, Any> = when {
    ttlMillis == null || ttlMillis < 30.minutes.inWholeMilliseconds || ttlMillis > 7.days.inWholeMilliseconds ->
        throw InvalidArgumentError("`ttlMillis` must be a duration in milliseconds between 30 minutes and 7 days (inclusive)")
    else ->
        Properties.encodeToMap(this)
}

private fun Any.encodeToBase64(encoder: Encoder = Base64.getEncoder()): String = encoder
    .encodeToString(Json.encodeToString(this).toByteArray())
    .replace(Regex("/"), "_")
    .replace(Regex("\\+"), "-")
    .replace(Regex("=+$"), "")


private fun InvalidArgumentError(message: String) = AppCheckError("InvalidArgument: $message")
