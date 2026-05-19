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
    namespace = "dev.ashdavies.playground.event.detail"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeMaterial)
            implementation(projects.feature.eventCommon)
            implementation(projects.identityManager)
            implementation(projects.placeholderHighlight)
            implementation(projects.sqlCommon)
            implementation(projects.uiComponents)

            implementation(libs.coil.compose)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.material3)
            implementation(libs.kotlinx.datetime)
            implementation(libs.slack.circuit.annotations)
            implementation(libs.slack.circuit.foundation)
        }
    }
}

metro {
    @OptIn(ExperimentalMetroGradleApi::class)
    enableCircuitCodegen = true
}
