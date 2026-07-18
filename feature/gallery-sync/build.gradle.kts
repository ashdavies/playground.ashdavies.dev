import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.zac.metro)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.gallery"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.httpClient)
            implementation(projects.sqlCommon)
            implementation(projects.uiComponents)

            implementation(libs.circuit.annotations)
            implementation(libs.circuit.foundation)
            implementation(libs.coil.compose)
            implementation(libs.compose.back.handler)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.sqldelight.coroutines.extensions)
        }

        commonTest.dependencies {
            implementation(libs.app.cash.turbine)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
        }
    }
}

metro {
    @OptIn(ExperimentalMetroGradleApi::class)
    enableCircuitCodegen = true
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
