package io.ashdavies.playground

import java.util.UUID

internal actual fun randomUuid(): String {
    return "${UUID.randomUUID()}"
}
