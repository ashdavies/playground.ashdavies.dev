package io.ashdavies.playground.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GitHub {

    interface Item<T> : ContentDescriptor, RemoteResource

    @Serializable
    data class StandardItem<T>(
        @SerialName("name") override val name: String,
        @SerialName("path") override val path: String,
        @SerialName("sha") override val sha: String,
        @SerialName("size") override val size: Int,
        @SerialName("url") override val url: String,
        @SerialName("html_url") override val htmlUrl: String,
        @SerialName("git_url") override val gitUrl: String,
        @SerialName("download_url") override val downloadUrl: String,
        @SerialName("type") override val type: String,
        @SerialName("_links") override val links: Links,
    ) : Item<T>

    @Serializable
    data class ContentItem<T>(
        @SerialName("delegate") private val delegate: Item<T>,
        @SerialName("content") override val content: String,
        @SerialName("encoding") override val encoding: String,
    ) : EncodedContent, Item<T> by delegate

    @Serializable
    data class Links(
        @SerialName("self") val self: String,
        @SerialName("git") val git: String,
        @SerialName("html") val html: String,
    )
}
