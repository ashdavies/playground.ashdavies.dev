package dev.ashdavies.content

public actual abstract class PlatformContext private constructor() {
    public companion object : PlatformContext()
}

public actual fun PlatformContext.isDebuggable(): Boolean = false
