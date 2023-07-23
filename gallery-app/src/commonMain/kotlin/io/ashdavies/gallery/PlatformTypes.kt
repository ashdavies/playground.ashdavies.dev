package io.ashdavies.gallery

public expect abstract class Context

public expect class File constructor(path: String) {
    public fun getAbsolutePath(): String
    public fun getName(): String
}

internal expect fun randomUuid(): String
