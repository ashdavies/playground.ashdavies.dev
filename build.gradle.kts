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
        classpath(cash.sqldelight)
        classpath(diffplug.spotless)
        classpath(google.services)

        with(kotlin) {
            classpath(compose)
            classpath(multiplatform)
            classpath(serialization)
        }

        alias(dependency.analysis)
        alias(gradle.doctor)
        alias(kotlinx.kover)
    }
}

dependencyAnalysis {
    structure {
        bundle("compose-runtime") {
            includeGroup(libs.compose.runtime)
        }
    }
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)
    javaHome { failOnError.set(false) }
}

val targetProject = project

subprojects {
    pluginManager.withPlugin("io.ashdavies.kotlin") {
        project.apply(plugin = "org.jetbrains.kotlinx.kover")
        targetProject.dependencies.kover(project)
    }
}
