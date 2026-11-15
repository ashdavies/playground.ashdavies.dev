plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.http.common)
            api(libs.ktor.client.auth)
        }
    }
}
