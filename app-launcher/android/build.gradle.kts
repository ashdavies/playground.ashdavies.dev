plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
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
    implementation(platform(libs.google.firebase.bom))

    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.google.firebase)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.google.accompanist.systemuicontroller)
    implementation(libs.google.android.material)
    implementation(libs.slack.circuit.foundation)
    implementation(libs.slack.circuit.overlay)

    implementation(projects.appLauncher.common) {
        exclude(libs.paging.compose.common)
    }

    implementation(projects.firebaseCompose)
}
