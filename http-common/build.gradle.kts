plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        compileOnly(libs.metro.runtime)

        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
    }
}
