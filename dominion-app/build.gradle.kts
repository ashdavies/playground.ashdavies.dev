plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.httpClient)
        implementation(projects.imageLoader)

        implementation(libs.slack.circuit.foundation)
        implementation(libs.slf4j.simple)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)

        with(libs.google) {
            implementation(accompanist.placeholderMaterial)

            implementation(dependencies.platform(firebase.bom))
            implementation(firebase.analytics)
            implementation(firebase.auth.ktx)
            implementation(firebase.common.ktx)
        }
    }
}
