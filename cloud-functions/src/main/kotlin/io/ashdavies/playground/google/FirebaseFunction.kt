package io.ashdavies.playground.google

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import kotlin.LazyThreadSafetyMode.NONE

private val _firebaseApp: FirebaseApp by lazy(NONE) {
    FirebaseApp.initializeApp()
}

interface FirebaseScope {
    val firebaseApp: FirebaseApp
}

abstract class FirebaseFunction : CoroutineFunction(), FirebaseScope {
    override val firebaseApp: FirebaseApp
        get() = _firebaseApp
}

val FirebaseScope.firestore: Firestore
    get() = FirestoreClient.getFirestore(firebaseApp)
