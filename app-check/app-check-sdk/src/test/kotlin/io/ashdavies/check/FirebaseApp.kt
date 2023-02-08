package io.ashdavies.check

import com.google.firebase.FirebaseApp

internal val firebaseApp: FirebaseApp by lazy(LazyThreadSafetyMode.NONE) {
    FirebaseApp.initializeApp()
}
