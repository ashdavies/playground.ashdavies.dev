package io.ashdavies.util

import java.util.UUID

public actual fun randomUuid(): String {
    return "${UUID.randomUUID()}"
}
