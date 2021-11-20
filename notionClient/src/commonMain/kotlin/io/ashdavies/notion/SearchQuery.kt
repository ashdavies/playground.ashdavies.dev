package io.ashdavies.notion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchQuery(
    @SerialName("query") val query: String? = null,
    @SerialName("sort") val sort: Sort? = null,
    @SerialName("filter") val filter: Filter? = null,
    @SerialName("startCursor") val startCursor: String? = null,
    @SerialName("pageSize") val pageSize: Int? = null,
) {

    @Serializable
    data class Sort(
        @SerialName("timestamp") val timestamp: Timestamp,
        @SerialName("direction") val direction: Direction? = null,
    ) {

        @Serializable
        enum class Direction {
            @SerialName("ascending") ASCENDING,
            @SerialName("descending") DESCENDING;
        }

        @Serializable
        enum class Timestamp {
            @SerialName("last_edited_time") LAST_EDITED_TIME;
        }
    }

    @Serializable
    data class Filter(
        @SerialName("property") val property: Property,
        @SerialName("value") val value: NotionType,
    ) {

        @Serializable
        enum class Property {
            @SerialName("object") OBJECT;
        }
    }
}
