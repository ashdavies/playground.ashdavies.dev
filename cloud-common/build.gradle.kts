plugins {
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    explicitApiWarning()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.asgService)
            implementation(projects.httpCommon)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.squareup.okio)
        }
    }
}
