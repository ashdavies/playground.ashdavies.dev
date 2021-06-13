package io.ashdavies.playground.firebase

@JsNonModule
@JsModule("firebase-admin")
external val DEFAULT_APP_NAME: String

@JsNonModule
@JsModule("firebase-admin")
external val admin: Admin

external interface Admin {
    val apps: Array<App>

    fun initializeApp(): App
    fun firestore(): Firestore
}

external interface App {
    val name: String
}

internal fun Admin.appOrNull(): App? {
    return apps.firstOrNull { it.name == DEFAULT_APP_NAME }
}