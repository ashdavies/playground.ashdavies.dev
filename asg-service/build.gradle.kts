plugins {
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpClient)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
    }
}
