package io.ashdavies.http

import android.os.Build

internal actual object Software {
    actual val buildVersion: String = Build.FINGERPRINT
    actual val productName: String = Build.PRODUCT
    actual val clientName: String = "Ktor/2.0.0"
    actual val gitCommit: String = Runtime
        .getRuntime()
        .exec("git rev-parse HEAD")
        .readText()
        .substring(0, 8)
}

private fun Process.readText() = inputStream
    .reader()
    .readText()
