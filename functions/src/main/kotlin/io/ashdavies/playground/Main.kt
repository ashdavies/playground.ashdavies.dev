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

    functions.get("/hello") { _: Request, res: Response<String> ->
        res.send("Hello World")
    }

    functions.get("/conferences") { _: Request, res: Response<String> ->
        val snapshot = Admin
            .firestore()
            .collection("conferences")
            .orderBy("dateStart", "desc")
            .limit(10)
            .get()

        res.send("OK")
    }

    val https: Https = Functions
        .region(EUROPE_WEST)
        .https

    exports.v1 = https.onRequest(functions)
}
