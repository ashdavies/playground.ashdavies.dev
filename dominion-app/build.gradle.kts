plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")

    alias(libs.plugins.cash.sqldelight)
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.circuitSupport)
        implementation(projects.httpClient)
        implementation(projects.platformScaffold)
        implementation(projects.platformSupport)
        implementation(projects.sqlDriver)

        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.coil.compose)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
        implementation(libs.paging.compose.common)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.sqldelight.paging3.extensions)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
        }
    }
}