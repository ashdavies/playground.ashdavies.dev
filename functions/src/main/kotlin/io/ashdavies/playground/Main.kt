package io.ashdavies.playground

import io.ashdavies.playground.events.EventsService
import io.ashdavies.playground.firebase.functions
import io.ashdavies.playground.test.HelloWorldService

private const val EUROPE_WEST = "europe-west1"

internal val exports: Exports
    get() = Exports

internal object Exports {
    lateinit var v1: Any
}

fun main() {
    exports.v1 = functions(region = EUROPE_WEST) {
        get("/hello", HelloWorldService())
        get("/events", EventsService())
    }
}
