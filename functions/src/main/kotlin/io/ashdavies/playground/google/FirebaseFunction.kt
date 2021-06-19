package io.ashdavies.playground.google

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseApp.initializeApp
import kotlin.LazyThreadSafetyMode.NONE

private val firebaseApp: FirebaseApp by lazy(NONE) { initializeApp() }

abstract class FirebaseFunction : CoroutineFunction() {
    init {
        firebaseApp
    }
}