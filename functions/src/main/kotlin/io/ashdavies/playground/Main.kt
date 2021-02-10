package io.ashdavies.playground

import io.ashdavies.playground.express.Express
import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.Functions
import io.ashdavies.playground.firebase.Https

external val exports: dynamic

private const val EUROPE_WEST = "europe-west1"

fun main() {
    Admin.initializeApp()

    val functions = Express()

    functions.get("/hello") { _: Request, response: Response<String> ->
        response.send("Hello World")
    }

    val https: Https = Functions
        .region(EUROPE_WEST)
        .https

    exports.v1 = https.onRequest(functions)
}
