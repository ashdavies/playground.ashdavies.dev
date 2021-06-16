package io.ashdavies.playground.firebase

import io.ashdavies.playground.express.Express

internal val functions: Functions
    get() = TODO()

internal interface Functions : FunctionBuilder {
    val logger: Logger
}

internal interface Logger {
    fun info(vararg args: Any)
    fun error(vararg args: Any)
}

internal interface FunctionBuilder {
    val https: Https

    fun region(vararg regions: String): FunctionBuilder
    fun runWith(runtimeOptions: RuntimeOptions): FunctionBuilder
}

internal interface Https {
    fun onRequest(express: Express): HttpsFunction
}

internal interface HttpsFunction

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
