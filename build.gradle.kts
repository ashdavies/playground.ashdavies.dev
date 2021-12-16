// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

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
        classpath(libs.android.gradlePlugin)
        classpath(libs.apache.batikExt)
        classpath(libs.apolloGraphQl.apolloGradlePlugin)
        classpath(libs.cash.molecule)
        classpath(libs.jetbrains.compose.gradlePlugin)
        classpath(libs.jetbrains.kotlin.gradlePlugin)
        classpath(libs.sqlDelight.kotlinGradlePlugin)
    }
}

plugins {
    alias(libs.plugins.anvil)
    alias(libs.plugins.gradle.doctor)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versions)
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

subprojects {
    tasks.withType<KotlinCompile<*>> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xallow-result-return-type",
            "-Xmulti-platform",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-XXLanguage:+InlineClasses",
        )
    }
}

doctor {
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
