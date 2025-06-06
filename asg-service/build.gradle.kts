plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpClient)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
    }
}
