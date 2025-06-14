plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
    }
}
