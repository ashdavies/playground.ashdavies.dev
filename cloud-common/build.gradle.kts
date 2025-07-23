plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.wasm")
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
