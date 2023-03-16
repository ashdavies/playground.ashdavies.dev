plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.kotlin")
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

    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.slack.circuit)
    implementation(libs.bundles.google.firebase)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.google.accompanist.systemuicontroller)

    implementation(projects.appLauncher.common) {
        exclude(libs.paging.compose.common)
    }

    implementation(projects.firebaseCompose)
}
