// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("compose-multiplatform")
    id(libs.plugins.android.library)
    id(libs.plugins.sqldelight)
    alias(libs.plugins.serialization)
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
    }

    sourceSets.forEach { sourceSet ->
        sourceSet.manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":composeLocal"))

                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.sqlDelight.coroutinesExtensions)
                implementation(libs.sqlDelight.runtime)
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
