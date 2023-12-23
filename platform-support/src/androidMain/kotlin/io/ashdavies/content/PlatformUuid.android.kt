package io.ashdavies.content

import java.util.UUID

public actual fun randomUuid(): String {
    return "${UUID.randomUUID()}"
}
