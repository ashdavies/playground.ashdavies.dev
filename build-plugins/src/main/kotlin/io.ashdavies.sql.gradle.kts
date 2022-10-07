@file:Suppress("UNUSED_VARIABLE")

import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.squareup.sqldelight")
    kotlin("multiplatform")
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            with(libs.sqldelight) {
                implementation(coroutines.extensions)
                implementation(runtime)
            }
        }
    }

    val androidMain by sourceSets.getting {
        dependencies {
            with(libs) {
                implementation(sqldelight.android.driver)
            }
        }
    }

    val jvmMain by sourceSets.getting {
        dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}
