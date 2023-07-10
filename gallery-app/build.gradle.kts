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

        implementation(libs.androidx.activity.compose)
        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
    }
}
