plugins {
    id("dev.ashdavies.default")
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
