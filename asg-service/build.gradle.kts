plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpClient)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
    }
}
