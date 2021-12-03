// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.android.library)
    id(libs.plugins.kotlin.multiplatform)
}

android {
    configurations {
        // https://youtrack.jetbrains.com/issue/KT-43944
        create("testApi", "testDebugApi", "testReleaseApi")
    }

    sourceSets.forEach { sourceSet ->
        sourceSet.manifest.srcFile("src/androidMain/AndroidManifest.xml")
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
                implementation(libs.sqlDelight.runtime)
            }
        }

        val androidMain by getting {
            dependencies {
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
