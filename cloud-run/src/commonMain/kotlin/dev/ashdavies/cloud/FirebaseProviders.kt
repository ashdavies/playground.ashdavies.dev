package dev.ashdavies.cloud

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import dev.ashdavies.check.AppCheck
import dev.ashdavies.check.appCheck
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient

@ContributesTo(AppScope::class)
internal interface FirebaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    @Suppress("SENSELESS_NULL_IN_WHEN")
    fun firebaseApp(): FirebaseApp {
        return when (val serviceAccountId = BuildConfig.GOOGLE_SERVICE_ACCOUNT_ID) {
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

    @Provides
    fun appCheck(firebaseApp: FirebaseApp, httpClient: HttpClient): AppCheck {
        return firebaseApp.appCheck(httpClient)
    }

    @Provides
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    @Provides
    fun firestore(firebaseApp: FirebaseApp): Firestore {
        return FirestoreClient.getFirestore(firebaseApp)
    }
}
