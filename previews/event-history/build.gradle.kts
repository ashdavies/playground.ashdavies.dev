plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.kotlin")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.event.history"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.asgService)
            implementation(projects.features.eventCommon)
            implementation(projects.features.eventHistory)
            implementation(projects.fixtures.events)
            implementation(projects.previews.tooling)

            implementation(libs.circuit.runtime)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
