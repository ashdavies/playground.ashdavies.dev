package io.ashdavies.notion

import java.util.UUID

actual fun randomUuid() = UUID
    .randomUUID()
    .toString()
