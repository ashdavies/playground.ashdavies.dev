@file:Suppress("UnusedReceiverParameter")

package dev.ashdavies.http.common.models

import io.ktor.http.HttpHeaders

public val HttpHeaders.XApiKey: String
    get() = "X-Api-Key"

public val HttpHeaders.XFirebaseAppCheck: String
    get() = "X-Firebase-AppCheck"
