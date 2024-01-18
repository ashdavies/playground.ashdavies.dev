plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.notion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)

        implementation(libs.jetbrains.kotlinx.serialization.core)
        implementation(libs.jetbrains.kotlinx.serialization.json)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.http)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.server.auth)
        implementation(libs.ktor.server.cio)
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.host.common)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set("io.ashdavies.notion")
            dependency(projects.localStorage)
        }
    }
}
