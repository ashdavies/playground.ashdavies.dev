package io.ashdavies.playground.firebase

import io.ashdavies.playground.express.Express

@JsNonModule
@JsModule("firebase-functions")
external object Functions {
    fun region(vararg regions: String): FunctionBuilder
}

external interface FunctionBuilder {
    val https: Https
}

external interface Https {
    fun onRequest(express: Express): HttpsFunction
}

external interface HttpsFunction

internal fun functions(region: String, block: Express.() -> Unit): HttpsFunction {
    val app: App = Admin.initializeApp()
    val https: Https = Functions
        .region(region)
        .https

    val express = Express().apply(block)
    return https.onRequest(express)
}
