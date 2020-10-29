package io.ashdavies.playground.network

interface RemoteResource {

    val downloadUrl: String
    val htmlUrl: String
    val gitUrl: String
    val url: String

    val links: GitHub.Links
}
