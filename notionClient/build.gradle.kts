// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.server.core)
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.slf4j)
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
                implementation(libs.jetbrains.kotlinx.coroutinesJdk)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.server.cio)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.jetbrains.kotlin.testCommon)
                implementation(libs.jetbrains.kotlin.testJunit)
            }
        }
    }
}
