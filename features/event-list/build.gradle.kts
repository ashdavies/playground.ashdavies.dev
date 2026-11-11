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
        namespace = "dev.ashdavies.playground.event.list"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.features.eventCommon)
            api(libs.androidx.paging.compose)
            api(libs.circuit.foundation)
            api(libs.compose.ui)

            implementation(projects.analytics)
            implementation(projects.composeMaterial)
            implementation(projects.core.ui.components)
            implementation(projects.core.ui.resources)
            implementation(projects.features.pagerFactory)

            implementation(libs.circuit.annotations)
            implementation(libs.coil.compose)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.shimmer)
            implementation(libs.kotlinx.collections.immutable)
        }

        commonTest.dependencies {
            implementation(projects.fixtures.events)

            implementation(libs.kotlin.test)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    generateContributionProviders = true
}
