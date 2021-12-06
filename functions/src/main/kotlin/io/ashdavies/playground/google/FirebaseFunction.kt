package io.ashdavies.playground.google

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseApp.initializeApp
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.coroutines.EmptyCoroutineContext

private val firebaseApp: FirebaseApp by lazy(NONE) { initializeApp() }

abstract class FirebaseFunction : CoroutineFunction(EmptyCoroutineContext) {
    init {
        firebaseApp
    }
}
