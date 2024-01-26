plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.properties")
}

android {
    defaultConfig {
        val androidApiKey by stringProperty { value ->
            manifestPlaceholders["ANDROID_API_KEY"] = value
        }
    }

    namespace = "io.ashdavies.routes"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)

        implementation(compose.material3)
        implementation(compose.runtime)

        implementation(libs.androidx.annotation)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.google.accompanist.permissions)
        implementation(libs.google.android.location)
        implementation(libs.google.maps.android.compose)
        implementation(libs.kotlinx.coroutines.play.services)
    }
}
