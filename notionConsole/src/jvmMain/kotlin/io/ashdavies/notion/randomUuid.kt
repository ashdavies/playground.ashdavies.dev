package io.ashdavies.notion

import java.util.UUID

actual fun randomUuid(): String = UUID
    .randomUUID()
    .toString()
