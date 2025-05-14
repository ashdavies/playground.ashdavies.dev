package io.ashdavies.content

import java.lang.management.ManagementFactory

private const val JAVA_DEBUG_WIRE_PROTOCOL = "jdwp"

public actual fun PlatformContext.isDebuggable(): Boolean {
    val args = ManagementFactory
        .getRuntimeMXBean()
        .inputArguments

    return args.any { it.contains(JAVA_DEBUG_WIRE_PROTOCOL) }
}

public actual fun PlatformContext.reportFullyDrawn(): Unit = Unit
