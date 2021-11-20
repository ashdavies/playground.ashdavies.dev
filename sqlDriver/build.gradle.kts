// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.sqlDelight.runtime)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqlDelight.sqliteDriver)
            }
        }
    }
}
