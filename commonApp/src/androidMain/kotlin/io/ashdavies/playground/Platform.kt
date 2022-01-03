package io.ashdavies.playground

import android.os.Build.VERSION.SDK_INT

actual object Platform {
    actual val platform: String get() = "Android $SDK_INT"
}
