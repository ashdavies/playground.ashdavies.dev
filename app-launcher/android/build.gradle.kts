plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.kotlin")

    kotlin("plugin.parcelize")
}

android {
    namespace = "io.ashdavies.playground"

    defaultConfig {
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
    implementation(projects.appLauncher.common)
    implementation(projects.firebaseCompose)

    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.arkivanov.decompose)
    implementation(libs.bundles.slack.circuit)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.google.accompanist.systemuicontroller)
}
