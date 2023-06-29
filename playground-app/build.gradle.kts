plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.base"
}

kotlin {
    commonMain.dependencies {
        with(projects) {
            implementation(appCheck.appCheckClient)
            implementation(cloudBackend.httpClient)
            implementation(localStorage)
        }

        implementation(libs.jetbrains.kotlinx.collections.immutable)

        with(libs.ktor.client) {
            implementation(content.negotiation)
            implementation(core)
            implementation(json)
            implementation(logging)
            implementation(okhttp3)
        }

        implementation(libs.slf4j.simple)
    }

    androidMain.dependencies {
        implementation(projects.firebaseCompose)

        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.jetbrains.kotlinx.coroutines.play)

        with(libs.google) {
            implementation(accompanist.placeholderMaterial)
            implementation(accompanist.systemuicontroller)
        }
    }
}
