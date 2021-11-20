package io.ashdavies.notion

import java.util.UUID

actual class UuidValue(private val uuid: UUID) {

    actual val short: String
        get() = uuid
            .toString()
            .substringBefore("-")

    actual override fun toString(): String =
        uuid.toString()

    actual companion object {
        actual fun fromString(name: String): UuidValue {
            return UuidValue(UUID.fromString(name))
        }

        actual fun randomUuid(): UuidValue {
            return UuidValue(UUID.randomUUID())
        }
    }
}
