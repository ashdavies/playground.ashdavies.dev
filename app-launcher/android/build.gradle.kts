plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
}

android {
    val androidTest by sourceSets.getting {
        res.srcDirs("src/androidMain/res")
    }

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
        implementation(projects.appLauncher.common)
        implementation(projects.httpClient)

        with(libs.androidx) {
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.activity.ktx)
        }

        implementation(libs.google.android.material)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.slack.circuit.overlay)
    }

    val androidInstrumentedTest by sourceSets.getting {
        dependsOn(androidMain)
    }
}
