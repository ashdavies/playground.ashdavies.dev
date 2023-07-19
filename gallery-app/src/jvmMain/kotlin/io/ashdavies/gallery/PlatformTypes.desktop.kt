package io.ashdavies.gallery

public actual abstract class Context {
    public companion object Default : Context()
}

public actual typealias File = java.io.File
