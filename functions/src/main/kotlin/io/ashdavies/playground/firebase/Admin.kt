package io.ashdavies.playground.firebase

@JsNonModule
@JsModule("firebase-admin")
external object Admin {
    fun initializeApp(): App
    fun firestore(): Firestore
}

external interface App