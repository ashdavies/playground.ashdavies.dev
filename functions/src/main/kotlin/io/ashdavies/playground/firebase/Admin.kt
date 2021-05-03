package io.ashdavies.playground.firebase

@JsNonModule
@JsModule("firebase-admin")
internal external object Admin {
    fun initializeApp(): App
    fun firestore(): Firestore
}

internal interface App {
    fun firestore(): Firestore
}