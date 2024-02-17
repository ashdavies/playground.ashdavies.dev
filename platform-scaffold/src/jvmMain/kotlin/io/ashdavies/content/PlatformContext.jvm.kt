package io.ashdavies.content

public actual abstract class PlatformContext {
    public companion object Default : PlatformContext()
}

public actual fun PlatformContext.reportFullyDrawn() = Unit
