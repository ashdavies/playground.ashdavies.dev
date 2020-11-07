package io.ashdavies.playground.network

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirestoreCollection<T>(
    @SerialName("documents") val documents: List<FirestoreDocument<T>>,
)

@Serializable(with = LocalDateTimeSerializer::class)
class FirestoreDocument<T>(
    @SerialName("name") val name: String,
    @SerialName("fields") val content: T,
    @SerialName("createTime") val createTime: LocalDateTime,
    @SerialName("updateTime") val updateTime: LocalDateTime,
)

@Serializable
data class FirestoreString(
    @SerialName("stringValue") val stringValue: String
)
