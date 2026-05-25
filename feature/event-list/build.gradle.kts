import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.compose.screenshot)
    alias(libs.plugins.zac.metro)
}

android {
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
    namespace = "dev.ashdavies.playground.event.list"
}

dependencies {
    screenshotTestImplementation(projects.asgService)
    screenshotTestImplementation(projects.feature.eventCommon)
    // screenshotTestImplementation(projects.feature.eventList)

    // screenshotTestImplementation(libs.androidx.paging.testing)
    screenshotTestImplementation(libs.kotlinx.collections.immutable)
    screenshotTestImplementation(libs.compose.screenshot.validation)
    screenshotTestImplementation(libs.compose.uiTooling)
    screenshotTestImplementation(libs.kotlinx.serialization.json)
    // screenshotTestImplementation(libs.sqldelight.sqlite.driver)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.composeMaterial)
            implementation(projects.feature.eventCommon)
            implementation(projects.pagingMultiplatform)
            implementation(projects.placeholderHighlight)
            implementation(projects.uiComponents)

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

    warnOnInjectAnnotationPlacement = false
}
