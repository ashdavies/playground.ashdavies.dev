// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.apache.batikExt)
        classpath(libs.apolloGraphQl.apolloGradlePlugin)
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
        mavenCentral()
    }

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
