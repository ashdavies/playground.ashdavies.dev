package io.ashdavies.notion

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

internal inline fun <reified T : Any> ColumnAdapter(): ColumnAdapter<T, String> = StringColumnAdapter(serializer())

internal class StringColumnAdapter<T : Any>(private val serializer: KSerializer<T>) : ColumnAdapter<T, String> {
    override fun decode(databaseValue: String): T = Json.decodeFromString(serializer, databaseValue)
    override fun encode(value: T): String = Json.encodeToString(serializer, value)
}
