package dev.ashdavies.http.common.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public data class EventAttrs(private val values: MutableMap<String, String?> = mutableMapOf()) {

    @Transient
    private val delegate = values.withDefault { null }

    public var imageUrl: String? by delegate
    public var status: String? by delegate
    public var cfpStart: String? by delegate
    public var cfpEnd: String? by delegate
    public var cfpSite: String? by delegate
    public var online: String? by delegate

    public fun toMap(): Map<String, String> = values.entries
        .mapNotNull { (key, value) -> value?.let { key to it } }
        .toMap()

    public companion object {
        public operator fun invoke(block: EventAttrs.() -> Unit): EventAttrs = EventAttrs(
            values = EventAttrs()
                .apply(block)
                .values
                .filterValues { it != null }
                .toMutableMap(),
        )
    }
}
