// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.compose.compose

plugins {
    id(libs.plugins.android.library)
    id(libs.plugins.kotlin.multiplatform)
    id(libs.plugins.sqldelight)

    alias(libs.plugins.serialization)
    group(libs.jetbrains.compose.gradlePlugin)
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    configurations {
        // https://youtrack.jetbrains.com/issue/KT-43944
        create("testApi", "testDebugApi", "testReleaseApi")
    }

    defaultConfig {
        compileSdk = 31
        targetSdk = 31
        minSdk = 23
    }

    sourceSets.forEach { sourceSet ->
        sourceSet.manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

kotlin {
    android()
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)

                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.sqlDelight.coroutinesExtensions)
                implementation(libs.sqlDelight.runtime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.jetbrains.kotlin.testAnnotations)
                implementation(libs.jetbrains.kotlin.testCommon)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
                implementation(libs.requery.sqliteAndroid)
                implementation(libs.sqlDelight.androidDriver)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqlDelight.sqliteDriver)
            }
        }
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
        dialect = "sqlite:3.25"
    }
}
