plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpClient)

        implementation(libs.coil.compose)
        implementation(libs.essenty.parcelable)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.slf4j.simple)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)

        with(libs.google) {
            implementation(accompanist.placeholderMaterial)
        }
    }
}
