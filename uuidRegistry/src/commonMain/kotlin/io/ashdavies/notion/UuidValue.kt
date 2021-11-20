package io.ashdavies.notion

expect class UuidValue {

    val short: String

    override fun toString(): String

    companion object {

        fun fromString(name: String): UuidValue

        fun randomUuid(): UuidValue
    }
}
