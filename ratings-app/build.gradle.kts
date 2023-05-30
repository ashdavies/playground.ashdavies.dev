plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.notion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.playgroundApp)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.jraf.klibnotion)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
