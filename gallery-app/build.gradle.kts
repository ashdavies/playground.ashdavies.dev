plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.gallery"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpClient)
        implementation(projects.imageLoader)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
    }
}
