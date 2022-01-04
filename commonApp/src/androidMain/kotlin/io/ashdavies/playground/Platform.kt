package io.ashdavies.playground

import android.os.Build.VERSION.SDK_INT

public actual object Platform {
    public actual val platform: String get() = "Android $SDK_INT"
}
