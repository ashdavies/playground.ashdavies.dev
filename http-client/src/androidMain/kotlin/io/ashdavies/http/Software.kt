package io.ashdavies.http

import android.os.Build

public actual object Software {
    public actual val buildVersion: String = Build.FINGERPRINT
    public actual val productName: String = Build.PRODUCT
    public actual val clientName: String = "Ktor/2.0.0"
}
