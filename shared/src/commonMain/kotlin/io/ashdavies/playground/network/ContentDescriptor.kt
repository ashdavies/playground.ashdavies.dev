package io.ashdavies.playground.network

interface ContentDescriptor : FileDescriptor {

    val sha: String
    val size: Int
}
