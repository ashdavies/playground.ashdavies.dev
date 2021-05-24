package io.ashdavies.playground.firebase

import io.ashdavies.playground.express.Express

@JsNonModule
@JsModule("firebase-functions")
external object Functions {
    fun config(): EnvironmentConfig
    fun firebaseConfig(): FirebaseConfig
    fun region(vararg regions: String): FunctionBuilder
}

external interface EnvironmentConfig {
    val github: GitHub

    interface GitHub {
        val key: String
    }
}

external interface FirebaseConfig {
    val storageBucket: String
    val databaseURL: String
    val projectId: String
}

external interface FunctionBuilder {
    val https: Https
}

external interface Https {
    fun onRequest(express: Express): HttpsFunction
}

external interface HttpsFunction

internal fun functions(region: String, block: Express.() -> Unit): HttpsFunction {
    @Suppress("UNUSED_VARIABLE")
    val app: App = Admin.initializeApp()
    val https: Https = Functions
        .region(region)
        .https

    val express = Express().apply(block)
    return https.onRequest(express)
}
