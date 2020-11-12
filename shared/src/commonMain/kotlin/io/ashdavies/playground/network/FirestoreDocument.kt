package io.ashdavies.playground.network

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirestoreDocument<T>(

    @SerialName("name")
    val name: String,

    @SerialName("fields")
    val content: T,

    @SerialName("createTime")
    @Serializable(LocalDateTimeSerializer::class)
    val createTime: LocalDateTime,

    @SerialName("updateTime")
    @Serializable(LocalDateTimeSerializer::class)
    val updateTime: LocalDateTime,
)
