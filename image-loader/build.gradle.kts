plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.graphics"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpClient)
        implementation(libs.ktor.client.core)
    }
}
