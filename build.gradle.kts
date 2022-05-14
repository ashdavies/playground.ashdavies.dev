// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        jetbrainsCompose()
        mavenCentral()
    }

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
    alias(libs.plugins.jetbrains.kotlinx.kover)
    alias(libs.plugins.gradle.doctor)
    alias(libs.plugins.gradle.ktlint)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        jetbrainsCompose()
        jitpack("requery")
        mavenCentral()
    }
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)

    javaHome {
        failOnError.set(false)
    }
}

fun isUnstable(version: String): Boolean {
    val unstableKeywords = listOf("ALPHA", "BETA", "RC")
    val upperVersion = version.toUpperCase()

    return unstableKeywords.any {
        upperVersion.contains(it)
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf { isUnstable(candidate.version) }
}

versionCatalogUpdate {
    pin { libraries.addAll(libs.android.tools.build.gradle, libs.jetbrains.kotlin.gradle.plugin) }
}
