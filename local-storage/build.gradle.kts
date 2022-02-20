// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("playground-android-library")
    id("playground-compose-multiplatform")

    id(libs.plugins.sqldelight)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":compose-local"))

                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.sqlDelight.coroutinesExtensions)
                implementation(libs.sqlDelight.runtime)
            }
        }

        val androidMain by getting {
            dependencies {
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
