import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.gallery"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.metroExtensions)
            implementation(projects.uiComponents)

            implementation(libs.circuit.annotations)
            implementation(libs.circuit.foundation)
            implementation(libs.coil.compose)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.navigation.event)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.metro.runtime.coroutines)
            implementation(libs.sqldelight.coroutines.extensions)
        }

        commonTest.dependencies {
            implementation(libs.app.cash.turbine)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    enableSuspendProviders = true
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            dialect(libs.sqldelight.sqlite.dialect)
            packageName = kotlin.android.namespace
            generateAsync = true
        }
    }
}
