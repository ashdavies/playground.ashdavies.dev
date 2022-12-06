plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.oauth"
}

kotlin {
    commonMain.dependencies {
        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.ktor.server)
    }
}
