package io.ashdavies.playground.firebase

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import io.ashdavies.playground.cloud.CoroutineFunction
import io.ashdavies.playground.google.DocumentProvider
import kotlin.LazyThreadSafetyMode.NONE

private val _firebaseApp: FirebaseApp by lazy(NONE) {
    FirebaseApp.initializeApp()
}

public interface FirebaseScope {
    public val firebaseApp: FirebaseApp
}

public abstract class FirebaseFunction : CoroutineFunction(), FirebaseScope {
    override val firebaseApp: FirebaseApp
        get() = _firebaseApp
}

public val FirebaseScope.firestore: Firestore
    get() = FirestoreClient.getFirestore(firebaseApp)

public fun FirebaseScope.DocumentProvider(path: String) = DocumentProvider {
    firestore.collection(path)
}
