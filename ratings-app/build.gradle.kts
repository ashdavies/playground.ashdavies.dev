plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.ratings"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.cloudBackend.httpClient)
        implementation(projects.notionClient)
        implementation(projects.playgroundApp)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.ktor.client.core)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
