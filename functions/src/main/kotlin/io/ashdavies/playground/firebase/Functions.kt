package io.ashdavies.playground.firebase

import io.ashdavies.playground.express.Express

@JsNonModule
@JsModule("firebase-functions")
internal external object Functions {
    fun region(vararg regions: String): FunctionBuilder
}

internal external interface FunctionBuilder {
    val https: Https
}

internal external interface Https {
    fun onRequest(express: Express): HttpsFunction
}

internal external interface HttpsFunction
