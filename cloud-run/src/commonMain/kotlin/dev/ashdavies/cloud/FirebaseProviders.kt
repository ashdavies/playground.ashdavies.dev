package dev.ashdavies.cloud

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
internal interface FirebaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    @Suppress("SENSELESS_NULL_IN_WHEN")
    fun firebaseApp(): FirebaseApp = FirebaseApp.getApps()
        .firstOrNull { it.name == FirebaseApp.DEFAULT_APP_NAME }
        ?: let {
            val googleServiceAccountId = BuildConfig.GOOGLE_SERVICE_ACCOUNT_ID
            val googleCloudProject = BuildConfig.GOOGLE_CLOUD_PROJECT

            if (googleServiceAccountId != null && googleCloudProject != null) {
                val firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setServiceAccountId(googleServiceAccountId)
                    .setProjectId(googleCloudProject)
                    .build()

                FirebaseApp.initializeApp(firebaseOptions)
            } else {
                FirebaseApp.initializeApp()
            }
        }

    @Provides
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    @Provides
    fun firestore(firebaseApp: FirebaseApp): Firestore {
        return FirestoreClient.getFirestore(firebaseApp)
    }

    @Provides
    @Named("events")
    fun eventsCollection(firestore: Firestore): CollectionReference {
        return firestore.collection("events")
    }
}
