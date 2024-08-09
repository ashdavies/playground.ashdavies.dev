plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.httpClient)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
    }
}
