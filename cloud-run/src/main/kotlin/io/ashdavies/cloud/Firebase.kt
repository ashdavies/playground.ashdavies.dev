package io.ashdavies.cloud

import com.google.firebase.FirebaseApp

internal val firebaseApp: FirebaseApp
    get() = FirebaseApp.getApps()
        .firstOrNull { it.name == FirebaseApp.DEFAULT_APP_NAME }
        ?: FirebaseApp.initializeApp()
