plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.httpClient)
        implementation(projects.imageLoader)

        implementation(libs.arkivanov.parcelable)

        with(libs.ktor.client) {
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.okhttp3)
        }

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
