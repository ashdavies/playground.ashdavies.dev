package dev.ashdavies.cloud

import dev.zacsweers.metro.createGraph

public fun main() {
    createGraph<CloudRunGraph>()
        .embeddedServer
        .start(true)
}
