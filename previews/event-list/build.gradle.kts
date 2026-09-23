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
        namespace = "dev.ashdavies.playground.event.list"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.eventList)
            implementation(projects.fixtures.events)

            implementation(libs.circuit.runtime)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
