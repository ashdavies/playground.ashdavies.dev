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

        pluginManager.withPlugin("io.ashdavies.android") {
            koverReport.defaults { mergeWith("debug") }
        }
    }
}

koverReport {
    defaults {
        verify {
            rule {
                minBound(3)
            }
        }
    }

    filters {
        excludes {
            classes("*.BuildConfig")
            classes("*.PlaygroundDatabase*")
            packages("io.ashdavies.generated.*")
        }
    }
}
