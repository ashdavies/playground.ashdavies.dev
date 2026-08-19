package dev.ashdavies.cloud

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
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
    fun firebaseApp(): FirebaseApp = FirebaseApp.initializeApp()

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
