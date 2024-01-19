plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpClient)

        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.coil.compose)
        implementation(libs.essenty.parcelable)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
        implementation(libs.slack.circuit.foundation)
    }
}
