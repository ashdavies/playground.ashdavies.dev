plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpCommon)
        implementation(projects.platformSupport)

        implementation(libs.kotlinx.serialization.properties)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.okhttp)
    }
}
