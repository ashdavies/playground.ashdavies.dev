@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage") // https://youtrack.jetbrains.com/issue/KTIJ-19369

// import com.diffplug.gradle.spotless.FormatExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    resolve(libs.plugins.android.application)
    resolve(libs.plugins.android.library)
    resolve(libs.plugins.apollo.graphql)
    resolve(libs.plugins.kotlin.compose)
    resolve(libs.plugins.kotlin.multiplatform)
    resolve(libs.plugins.kotlin.serialization)

    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.catalog.update)
    // alias(libs.plugins.diffplug.spotless)
    alias(libs.plugins.gradle.doctor)
    alias(libs.plugins.kotlinx.kover)
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)
    javaHome { failOnError.set(false) }
}

// spotless {
//     val ktlintVersion: String = libs.versions.pinterest.ktlint.get()
//     fun FormatExtension.kotlinDefault(extension: String = "kt") {
//         targetExclude("**/build/**")
//         target("src/**/*.$extension")
//         trimTrailingWhitespace()
//         endWithNewline()
//     }
//
//     kotlinGradle {
//         ktlint(ktlintVersion)
//             .editorConfigOverride(mapOf("disabled_rules" to "filename"))
//             .userData(mapOf("android" to "true"))
//             .setUseExperimental(true)
//
//         kotlinDefault("kts")
//     }
//
//     kotlin {
//         ktlint(ktlintVersion)
//             .editorConfigOverride(mapOf("disabled_rules" to "filename"))
//             .userData(mapOf("android" to "true"))
//             .setUseExperimental(true)
//
//         kotlinDefault("kt")
//     }
//
//     format("terraform") {
//         target("src/main/terraform/**/*.tf")
//         custom("terraform") { fileContents ->
//             terraformExec {
//                 stdin(fileContents)
//                 args("fmt", "-")
//             }
//         }
//     }
// }

versionCatalogUpdate {
    pin {
        versions.addAll(
            libs.versions.jetbrains.compose.get(), // Unstable until 1.2.0-alpha01-dev686
            libs.versions.google.android.get(), // JetBrains Compose Plugin Compatibility
        )
    }
}

tasks.withType<DependencyUpdatesTask> {
    fun isUnstable(version: String): Boolean {
        val unstableKeywords = listOf("ALPHA", "BETA", "RC")
        val upperVersion = version.toUpperCase()

        return unstableKeywords.any {
            upperVersion.contains(it)
        }
    }

    rejectVersionIf {
        isUnstable(candidate.version)
    }
}
