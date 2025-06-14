package io.ashdavies.content

public actual abstract class PlatformContext private constructor() {
    public companion object Default : PlatformContext()
}
