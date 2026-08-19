plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpCommon)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
    }
}
