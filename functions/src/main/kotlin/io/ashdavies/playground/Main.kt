package io.ashdavies.playground

import io.ashdavies.playground.firebase.functions
import io.ashdavies.playground.service.HelloWorldService
import io.ashdavies.playground.service.SyncConferencesService

external val exports: dynamic

fun main() {
    exports.v1 = functions(region = "europe-west1") {
        get("/hello", HelloWorldService)
        get("/sync", SyncConferencesService)
    }
}
