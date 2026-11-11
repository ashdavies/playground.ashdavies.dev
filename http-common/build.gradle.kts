plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.kotlinx.datetime)
        api(libs.kotlinx.serialization.core)
        api(libs.ktor.http)

        compileOnly(libs.metro.runtime) // TODO Move to dedicated module with api
    }
}
