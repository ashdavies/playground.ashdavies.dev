plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "ANDROID_API_KEY", "\"${System.getenv("ANDROID_API_KEY")}\"")

        versionName = "1.0"
        versionCode = 1
    }

    namespace = "io.ashdavies.playground"
}

kotlin {
    androidDebug.dependencies {
        implementation(libs.squareup.leakcanary)
    }

    androidMain.dependencies {
        with(projects) {
            implementation(appLauncher.common)
            implementation(httpClient)
        }

        with(libs.androidx) {
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.activity.ktx)
        }

        with(libs.google) {
            implementation(android.material)
        }

        implementation(libs.slack.circuit.foundation)
        implementation(libs.slack.circuit.overlay)
    }
}
