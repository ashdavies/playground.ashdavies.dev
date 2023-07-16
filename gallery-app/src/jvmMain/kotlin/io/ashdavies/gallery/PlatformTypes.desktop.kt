package io.ashdavies.gallery

public actual abstract class Context {
    public companion object Default : Context()
}

public actual abstract class Uri {
    public abstract val value: java.net.URI
}
