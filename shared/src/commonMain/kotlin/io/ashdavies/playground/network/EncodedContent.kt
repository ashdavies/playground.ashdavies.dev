package io.ashdavies.playground.network

interface EncodedContent : RemoteResource {

    val content: String
    val encoding: String
}
