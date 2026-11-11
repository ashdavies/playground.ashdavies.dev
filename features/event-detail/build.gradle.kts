import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.event.detail"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.features.eventCommon)
            api(libs.compose.ui)

            implementation(projects.composeMaterial)
            implementation(projects.core.ui.components)
            implementation(projects.core.ui.resources)
            implementation(projects.identityManager)
            implementation(projects.metroExtensions)

            implementation(libs.circuit.annotations)
            implementation(libs.circuit.foundation)
            implementation(libs.coil.compose)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.shimmer)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    enableSuspendProviders = true
}
