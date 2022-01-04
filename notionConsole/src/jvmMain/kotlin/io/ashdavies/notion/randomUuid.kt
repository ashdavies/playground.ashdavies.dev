package io.ashdavies.notion

import java.util.UUID

public actual fun randomUuid(): String = UUID
    .randomUUID()
    .toString()
