package dev.ashdavies.playground.event

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
public data class EventAttrs(private val values: Map<String, String?>) {

    public constructor(values: Map<String, String>) : this(values.withDefault { null })

    public val imageUrl: String? by values
    public val status: String? by values
    public val cfpStart: String? by values
    public val cfpEnd: String? by values
    public val cfpSite: String? by values

    public val online: Boolean get() = values["online"].toBoolean()

    public object Adapter : ColumnAdapter<EventAttrs, String> {
        override fun decode(databaseValue: String): EventAttrs = EventAttrs(Json.decodeFromString(databaseValue))
        override fun encode(value: EventAttrs): String = Json.encodeToString(value.values)
    }
}
