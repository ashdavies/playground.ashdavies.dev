package io.ashdavies.playground.firebase

import io.ashdavies.playground.express.Express

/**
 * Something here is going wrong, revert to compare changes!
 * Requests are timing out after 60 seconds
 *
 * Errors came after introducing interface for FunctionBiulder
 */
@JsNonModule
@JsModule("firebase-functions")
external val functions: Functions

external interface Functions : FunctionBuilder {
    val logger: Logger
}

external interface Logger {
    fun info(vararg args: Any)
    fun error(vararg args: Any)
}

external interface FunctionBuilder {
    val https: Https

    fun region(vararg regions: String): FunctionBuilder
    fun runWith(runtimeOptions: RuntimeOptions): FunctionBuilder
}

external interface Https {
    fun onRequest(express: Express): HttpsFunction
}

external interface HttpsFunction

data class RuntimeOptions(val timeoutSeconds: Number? = null)

internal fun functions(region: String, block: Express.() -> Unit): HttpsFunction {
    admin.appOrNull() ?: admin.initializeApp()

    val https: Https = functions
        //.runWith(RuntimeOptions(timeoutSeconds = 30))
        .region(region)
        .https

    val express = Express().apply(block)
    return https.onRequest(express)
}
