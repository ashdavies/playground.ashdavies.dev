plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
}

android {
    namespace = "io.ashdavies.playground"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigString("PLAYGROUND_API_KEY") { stringPropertyOrNull("playground.api.key") }

        versionName = "1.0"
        versionCode = 1
    }

    val main by sourceSets.getting {
        // Overwrite manifest.srcFile io.ashdavies.android.gradle.kts:56
        manifest.srcFile("src/main/AndroidManifest.xml")
    }
}

dependencies {
    implementation(projects.appLauncher.common)
    implementation(projects.firebaseCompose)

    with(libs.androidx) {
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
    }

    with(libs.google) {
        implementation(android.material)

        implementation(platform(firebase.bom))
        implementation(firebase.analytics)
        implementation(firebase.common.ktx)
    }

    implementation(libs.slack.circuit.foundation)
    implementation(libs.slack.circuit.overlay)
}
