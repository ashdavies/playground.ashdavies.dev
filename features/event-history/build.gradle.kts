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
        namespace = "dev.ashdavies.playground.event.grid"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.ui)
            api(libs.kotlinx.collections.immutable)

            implementation(projects.cloudCommon)
            implementation(projects.composeMaterial)
            implementation(projects.core.ui.components)
            implementation(projects.core.ui.resources)
            implementation(projects.features.eventCommon)
            implementation(projects.metroExtensions)

            implementation(libs.androidx.window.core)
            implementation(libs.circuit.annotations)
            implementation(libs.circuit.foundation)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.sqldelight.coroutines.extensions)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    enableSuspendProviders = true
}
