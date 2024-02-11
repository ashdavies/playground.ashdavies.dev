plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")

    alias(libs.plugins.openapi.generator)
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpCommon)

        implementation(compose.runtime)

        implementation(libs.kotlinx.serialization.properties)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
    }
}
