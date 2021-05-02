package io.ashdavies.playground.firebase

@JsNonModule
@JsModule("firebase-admin")
internal external object Admin {
    fun initializeApp()
    fun firestore(): Firestore
}
