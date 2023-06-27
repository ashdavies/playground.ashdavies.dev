plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.ratings"
}

kotlin {
    commonMain.dependencies {
        with(projects) {
            implementation(cloudBackend.httpClient)
            implementation(notionClient)
            implementation(playgroundApp)
        }

        implementation(libs.arkivanov.parcelable)
        implementation(libs.ktor.client.core)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
    }
}
