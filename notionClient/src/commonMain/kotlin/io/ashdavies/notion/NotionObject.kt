package io.ashdavies.notion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotionObject(
    @SerialName("id") val id: String,
    @SerialName("object") val type: NotionType,
    @SerialName("title") val title: List<Title>,
) {

    @Serializable
    data class Title(
        @SerialName("plain_text") val plainText: String,
    )
}
