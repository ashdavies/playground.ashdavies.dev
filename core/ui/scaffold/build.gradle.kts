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
            implementation(projects.composeMaterial)
            implementation(projects.core.ui.snackbar)
            implementation(projects.previews.tooling)

            implementation(libs.circuit.foundation)
            implementation(libs.circuit.runtime)

            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.navigation.event)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    enableSuspendProviders = true
}
