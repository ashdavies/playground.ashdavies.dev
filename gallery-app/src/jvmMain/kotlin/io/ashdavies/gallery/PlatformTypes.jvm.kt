package io.ashdavies.gallery

import java.util.UUID

public actual typealias File = java.io.File

internal actual fun randomUuid(): String {
    return "${UUID.randomUUID()}"
}
