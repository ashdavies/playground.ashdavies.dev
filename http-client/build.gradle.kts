import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("io.ashdavies.default")

    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "io.ashdavies.http.client"
}

kotlin {
    commonMain {
        dependencies {
            api(projects.httpCommon)
            api(libs.ktor.client.core)
            api(libs.ktor.client.logging)

            implementation(projects.localStorage)

            implementation(libs.jetbrains.kotlinx.serialization.properties)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.serialization.kotlinx)
            implementation(libs.slf4j.simple)
        }
    }
}
