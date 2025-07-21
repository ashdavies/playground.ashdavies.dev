package io.ashdavies.content

import android.content.pm.ApplicationInfo

public actual fun PlatformContext.isDebuggable(): Boolean {
    return applicationInfo.flags != 0 and ApplicationInfo.FLAG_DEBUGGABLE
}
