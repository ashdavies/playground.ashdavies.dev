plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.routing"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpClient)
        implementation(projects.httpCommon)

        compileOnly(libs.google.maps.routing)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.ktor.client.core)
    }
}
