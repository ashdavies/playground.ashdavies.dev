package io.ashdavies.playground

import io.ashdavies.playground.firebase.functions
import io.ashdavies.playground.events.EventsService
import io.ashdavies.playground.test.HelloWorldService

private const val EUROPE_WEST = "europe-west1"

external val exports: dynamic

fun main() {
    exports.v1 = functions(region = EUROPE_WEST) {
        get("/conferences", EventsService())
        get("/hello", HelloWorldService())
    }
}