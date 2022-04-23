@file:Suppress("UNUSED_VARIABLE")

import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.squareup.sqldelight")
    kotlin("multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                with(libs.sqldelight) {
                    implementation(coroutines.extensions)
                    implementation(runtime)
                }
            }
        }

        val androidMain by getting {
            dependencies {
                with(libs) {
                    implementation(requery.sqlite.android)
                    implementation(sqldelight.android.driver)
                }
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
    }

}
