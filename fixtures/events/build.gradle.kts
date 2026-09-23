plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.fixtures")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.event.fixtures"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.features.eventCommon)

            implementation(projects.asgService)

            implementation(libs.kotlinx.serialization.json)
        }
    }
}
