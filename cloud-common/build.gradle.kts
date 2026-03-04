plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.asgService)
        implementation(projects.httpCommon)

        implementation(libs.kotlinx.serialization.json)
        implementation(libs.squareup.okio)
    }
}
