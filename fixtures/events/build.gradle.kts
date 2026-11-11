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
            api(libs.kotlinx.serialization.json)

            implementation(projects.asgService)
            implementation(projects.cloudCommon)
            implementation(projects.httpCommon)
            implementation(projects.features.eventCommon)

            implementation(libs.kotlinx.datetime)
        }
    }
}
