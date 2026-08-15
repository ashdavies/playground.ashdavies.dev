plugins {
    fun classpath(notation: Provider<PluginDependency>) = alias(notation) apply false

    with(libs.plugins) {
        classpath(android.application)
        classpath(android.library)
        classpath(cash.sqldelight)
        classpath(compose.compiler)
        classpath(detekt)
        classpath(firebase.crashlytics)
        classpath(google.services)
        classpath(jetbrains.compose)
        classpath(kotlin.multiplatform)
        classpath(kotlin.serialization)
        classpath(kotlinx.kover)
        classpath(ktlint)
    }
}
