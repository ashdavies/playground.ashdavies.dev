// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.android.build.api.dsl.AndroidSourceSet

plugins {
    id(libs.plugins.android.library)
    id(libs.plugins.kotlin.multiplatform)
    id(libs.plugins.sqldelight)

    alias(libs.plugins.serialization)
}

android {
    configurations {
        // https://youtrack.jetbrains.com/issue/KT-43944
        //create("testApi", "testDebugApi", "testReleaseApi")
    }

    sourceSets.forEach { sourceSet ->
        sourceSet.manifest("src/androidMain/AndroidManifest.xml")
    }

    defaultConfig {
        compileSdk = 30
        minSdk = 21
        targetSdk = 30
    }
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
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

        val jvmMain by getting {
            dependencies {
                //implementation(libs.sqlDelight.sqliteDriver)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.coreKtx)
                implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
                implementation(libs.ktor.client.android)
                implementation(libs.sqlDelight.androidDriver)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.jetbrains.kotlin.test)
                implementation(libs.jetbrains.kotlin.testJunit)
            }
        }
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground.database"
    }
}

fun NamedDomainObjectContainer<*>.create(vararg names: String) {
    names.forEach { create(it) }
}

fun AndroidSourceSet.manifest(srcPath: String) {
    manifest.srcFile(srcPath)
}
