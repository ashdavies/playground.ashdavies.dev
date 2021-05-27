package io.ashdavies.playground.firebase

import io.ashdavies.playground.express.Express

@JsNonModule
@JsModule("firebase-functions")
external object Functions {
    val logger: Logger

    fun <T : Config> config(): T
    fun region(vararg regions: String): FunctionBuilder
}

external interface Config

external interface EnvironmentConfig : Config {
    val github: GitHub

    interface GitHub {
        val key: String
    }
}

external interface Logger {
    fun info(vararg args: Any)
    fun error(vararg args: Any)
}

external interface FunctionBuilder {
    val https: Https
}

external interface Https {
    fun onRequest(express: Express): HttpsFunction
}

external interface HttpsFunction

@Suppress("UNUSED_VARIABLE")
internal fun functions(region: String, block: Express.() -> Unit): HttpsFunction {
    val https: Https = Functions
        .region(region)
        .https

    val express = Express().apply(block)
    return https.onRequest(express)
}
