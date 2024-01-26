plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")

    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "io.ashdavies.http.client"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpCommon)
        implementation(projects.localStorage)

        implementation(compose.runtime)

        implementation(libs.kotlinx.serialization.properties)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
    }
}
