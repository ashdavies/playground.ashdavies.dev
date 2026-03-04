plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

android {
    namespace = "dev.ashdavies.routing"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpClient)
        implementation(projects.httpCommon)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.ktor.client.core)
    }
}
