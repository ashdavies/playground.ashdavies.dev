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
        implementation(projects.analytics)
        implementation(projects.httpClient)
        implementation(projects.pagingCompose)
        implementation(projects.platformSupport)
        implementation(projects.sqlCommon)
        implementation(projects.sqlCompose)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.ui)

        implementation(libs.androidx.paging.common)
        implementation(libs.coil.compose)
        implementation(libs.compose.adaptive.layout)
        implementation(libs.compose.adaptive.navigation)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
        implementation(libs.slack.circuit.foundation)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
        }
    }
}
