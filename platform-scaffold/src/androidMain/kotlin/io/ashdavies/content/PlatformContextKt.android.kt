package io.ashdavies.content

import android.app.Activity
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo

private val PlatformContext.activity: Activity
    get() = requireNotNull(findActivity()) { "Could not find activity!" }

public fun PlatformContext.isDebuggable(): Boolean {
    return applicationInfo.flags != 0 and ApplicationInfo.FLAG_DEBUGGABLE
}

public actual fun PlatformContext.reportFullyDrawn() {
    activity.reportFullyDrawn()
}

private fun PlatformContext.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
