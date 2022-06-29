package io.ashdavies.http

import android.os.Build

internal actual object Software {
    actual val clientName: String = BuildConfig.CLIENT_NAME
    actual val buildVersion: String = Build.FINGERPRINT
    actual val productName: String = Build.PRODUCT
}
