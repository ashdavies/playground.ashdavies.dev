import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.zac.metro)
}

android {
    namespace = "dev.ashdavies.playground.event.grid"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.asgService)
            implementation(projects.cloudCommon)
            implementation(projects.composeMaterial)
            implementation(projects.feature.eventCommon)
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.platformSupport)
            implementation(projects.sqlCommon)
            implementation(projects.sqlDriver)
            implementation(projects.uiComponents)

            implementation(libs.androidx.window.core)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.slack.circuit.annotations)
            implementation(libs.slack.circuit.foundation)
            implementation(libs.sqldelight.coroutines.extensions)
        }
    }
}

metro {
    @OptIn(ExperimentalMetroGradleApi::class)
    enableCircuitCodegen = true

    warnOnInjectAnnotationPlacement = false
}
