import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.zac.metro)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.event.list"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.composeMaterial)
            implementation(projects.feature.eventCommon)
            implementation(projects.feature.pagerFactory)
            implementation(projects.placeholderHighlight)
            implementation(projects.uiComponents)

            implementation(libs.androidx.paging.compose)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.coil.compose)
            implementation(libs.kotlinx.collections.immutable)
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
