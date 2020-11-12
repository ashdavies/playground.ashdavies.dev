package io.ashdavies.playground.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirestoreCollection<T>(

    @SerialName("documents")
    val documents: List<FirestoreDocument<T>>
)
