import com.android.build.api.dsl.VariantDimension

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
        buildConfigString("CLIENT_NAME", "Ktor/${libs.versions.ktor.get()}")
        buildConfigString("PLAYGROUND_API_KEY", "AIzaSyAq4aV6fszZG6CudbbAnOSFy--GD7vjJwA")

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
    implementation(libs.slack.circuit.foundation)
    implementation(libs.slack.circuit.overlay)

    implementation(projects.appLauncher.common) {
        exclude(libs.paging.compose.common)
    }

    implementation(projects.firebaseCompose)
}

fun VariantDimension.buildConfigString(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}
