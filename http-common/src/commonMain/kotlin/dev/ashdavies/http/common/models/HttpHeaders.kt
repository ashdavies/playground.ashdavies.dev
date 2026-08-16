@file:Suppress("UnusedReceiverParameter")

package dev.ashdavies.http.common.models

import io.ktor.http.HttpHeaders

public val HttpHeaders.XAndroidCert: String
    get() = "X-Android-Cert"

public val HttpHeaders.XAndroidPackage: String
    get() = "X-Android-Package"

public val HttpHeaders.XApiKey: String
    get() = "X-Api-Key"

public val HttpHeaders.XFirebaseAppCheck: String
    get() = "X-Firebase-AppCheck"

public val HttpHeaders.XVersionName: String
    get() = "X-Version-Name"
