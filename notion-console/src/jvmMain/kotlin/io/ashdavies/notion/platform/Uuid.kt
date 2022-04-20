package io.ashdavies.notion.platform

import java.util.UUID

public actual fun randomUuid(): String = UUID
    .randomUUID()
    .toString()
