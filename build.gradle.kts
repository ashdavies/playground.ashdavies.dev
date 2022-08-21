// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

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
    kotlinGradle {
        ktlint("0.45.2").userData(mapOf("android" to "true"))
        kotlinDefault()
    }

    kotlin {
        ktlint("0.45.2").userData(mapOf("android" to "true"))
        kotlinDefault()
    }
}

versionCatalogUpdate {
    pin { libraries.addAll(libs.android.tools.build.gradle, libs.jetbrains.kotlin.gradle.plugin) }
}

fun isUnstable(version: String): Boolean {
    val unstableKeywords = listOf("ALPHA", "BETA", "RC")
    val upperVersion = version.toUpperCase()

    return unstableKeywords.any {
        upperVersion.contains(it)
    }
}

fun FormatExtension.kotlinDefault() {
    targetExclude("**/build/**/*.kt")
    target("src/**/*.kt")
    trimTrailingWhitespace()
    endWithNewline()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf { isUnstable(candidate.version) }
}
