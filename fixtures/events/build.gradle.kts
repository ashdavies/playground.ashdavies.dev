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
            api(projects.asgService)
            api(projects.features.eventCommon)
            api(libs.kotlinx.serialization.json)

            implementation(projects.cloudCommon)
        }
    }
}
