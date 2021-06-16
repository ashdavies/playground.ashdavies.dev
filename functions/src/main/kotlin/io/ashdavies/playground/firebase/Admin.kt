package io.ashdavies.playground.firebase

internal val DEFAULT_APP_NAME: String
    get() = "[DEFAULT_APP]"

internal val admin: Admin
    get() = TODO()

internal object Admin {
    val apps: Array<App> = emptyArray()

    fun initializeApp() = App(DEFAULT_APP_NAME)
    fun firestore(): Firestore = TODO()
}

internal data class App(val name: String)

internal fun Admin.appOrNull(): App? {
    return apps.firstOrNull { it.name == "DEFAULT_APP_NAME" }
}