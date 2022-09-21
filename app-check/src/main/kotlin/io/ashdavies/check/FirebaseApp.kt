package io.ashdavies.check

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

private const val GET_CREDENTIALS_METHOD = "getCredentials"

internal val FirebaseApp.credentials: GoogleCredentials
    get() = FirebaseOptions::class.java
        .getDeclaredMethod(GET_CREDENTIALS_METHOD)
        .also { it.isAccessible = true }
        .invoke(options) as GoogleCredentials
