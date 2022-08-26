@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage") // https://youtrack.jetbrains.com/issue/KTIJ-19369

import com.diffplug.gradle.spotless.FormatExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    dependencies {
        with(libs.versions.jetbrains.kotlin) {
            classpath(kotlin("gradle-plugin", version = get()))
            classpath(kotlin("serialization", version = get()))
        }

        classpath(libs.android.tools.build.gradle)
        classpath(libs.apache.batikExt)
        classpath(libs.apollographql.apollo.gradle.plugin)
        classpath(libs.jetbrains.compose.gradle.plugin)
        classpath(libs.jetbrains.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.diffplug.spotless)
    alias(libs.plugins.jetbrains.kotlinx.kover)
    alias(libs.plugins.gradle.doctor)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)
    javaHome { failOnError.set(false) }
}

spotless {
    val ktlintVersion: String = libs.versions.pinterest.ktlint.get()
    fun FormatExtension.kotlinDefault(extension: String = "kt") {
        targetExclude("**/build/**")
        // target("**/*.$extension")
        target("src/**/*.$extension")
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        ktlint(ktlintVersion)
            .editorConfigOverride(mapOf("disabled_rules" to "filename"))
            .userData(mapOf("android" to "true"))
            .setUseExperimental(true)

        kotlinDefault("kts")
    }

    kotlin {
        ktlint(ktlintVersion)
            .editorConfigOverride(mapOf("disabled_rules" to "filename"))
            .userData(mapOf("android" to "true"))
            .setUseExperimental(true)

        kotlinDefault("kt")
    }
}

versionCatalogUpdate {
    pin {
        libraries.addAll(
            libs.jetbrains.kotlin.gradle.plugin.asProvider(), // Unstable until 1.2.0-alpha01-dev686
            libs.android.tools.build.gradle, // Compatibility with JetBrains Compose Plugin
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
