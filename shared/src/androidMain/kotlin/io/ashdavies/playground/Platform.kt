package io.ashdavies.playground

import android.os.Build.VERSION.SDK_INT

actual object Platform {
    actual val platform: String = "Android $SDK_INT"
}
