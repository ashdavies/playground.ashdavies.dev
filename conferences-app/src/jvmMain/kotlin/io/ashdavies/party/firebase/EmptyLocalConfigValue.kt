package io.ashdavies.party.firebase

import io.ashdavies.config.LocalConfigValue

internal object EmptyLocalConfigValue : LocalConfigValue {
    override fun asLong() = 0L
    override fun asDouble() = 0.0
    override fun asString() = ""
    override fun asByteArray() = byteArrayOf()
    override fun asBoolean() = false
}
