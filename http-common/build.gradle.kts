plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
    }
}
