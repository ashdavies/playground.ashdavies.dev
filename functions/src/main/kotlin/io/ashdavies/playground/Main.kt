package io.ashdavies.playground

import io.ashdavies.playground.firebase.functions
import io.ashdavies.playground.service.ConferencesService
import io.ashdavies.playground.service.HelloWorldService

private const val EUROPE_WEST = "europe-west1"

external val exports: dynamic

fun main() {
    exports.v1 = functions(region = EUROPE_WEST) {
        get("/conferences", ConferencesService)
        get("/hello", HelloWorldService)
    }
}
