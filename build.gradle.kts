@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

buildscript {
    dependencies {
        val googleServicesVersion = libs.versions.google.services.get()
        classpath("com.google.gms:google-services:$googleServicesVersion")
    }
}

plugins {
    fun classpath(notation: Provider<PluginDependency>) = alias(notation) apply false

    with(libs.plugins) {
        classpath(android.application)
        classpath(android.library)
        classpath(apollo.graphql)
        classpath(diffplug.spotless)
        classpath(google.services)
        classpath(kotlinx.kover)

        with(kotlin) {
            classpath(compose)
            classpath(multiplatform)
            classpath(serialization)
        }

        alias(dependency.analysis)
        alias(gradle.doctor)
    }
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)
    javaHome { failOnError.set(false) }
}
