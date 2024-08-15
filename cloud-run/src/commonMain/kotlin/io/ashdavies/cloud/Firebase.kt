package io.ashdavies.cloud

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

internal val firebaseApp: FirebaseApp by lazy(LazyThreadSafetyMode.NONE) {
    when (val serviceAccountId = BuildConfig.GOOGLE_SERVICE_ACCOUNT_ID) {
        null -> FirebaseApp.initializeApp()
        else -> {
            val firebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setServiceAccountId(serviceAccountId)
                .build()

            FirebaseApp.initializeApp(firebaseOptions)
        }
    }
}
