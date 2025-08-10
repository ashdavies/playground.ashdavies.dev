plugins {
    fun classpath(notation: Provider<PluginDependency>) = alias(notation) apply false

    with(libs.plugins) {
        classpath(android.application)
        classpath(android.library)
        classpath(cash.sqldelight)
        classpath(compose.compiler)
        classpath(detekt)
        classpath(crashlytics)
        classpath(google.services)
        classpath(jetbrains.compose)
        classpath(kotlin.multiplatform)
        classpath(ktlint)

        alias(gradle.doctor)
        alias(kotlinx.kover)
    }
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)
    javaHome { failOnError.set(false) }
}

subprojects {
    pluginManager.withPlugin("dev.ashdavies.kotlin") {
        project.apply(plugin = "org.jetbrains.kotlinx.kover")
        rootProject.dependencies.kover(project)
    }
}
