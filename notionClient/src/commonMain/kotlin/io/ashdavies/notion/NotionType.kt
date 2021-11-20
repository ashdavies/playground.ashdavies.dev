package io.ashdavies.notion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NotionType {
    @SerialName("database") DATABASE,
    @SerialName("list") LIST,
    @SerialName("page") PAGE,
}
