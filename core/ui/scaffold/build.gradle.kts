import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.metro)
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.scaffold"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.ui.snackbar)

            api(libs.circuit.foundation)
            api(libs.compose.components.resources)
            api(libs.kotlinx.collections.immutable)

            implementation(projects.composeMaterial)
            implementation(projects.core.ui.resources)
            implementation(projects.previews.tooling)

            implementation(libs.circuit.serialization)

            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.navigation.event)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    enableSuspendProviders = true
}
