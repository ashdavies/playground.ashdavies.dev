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
        val androidApiKey = System.getenv("ANDROID_API_KEY")
        buildConfigField("String", "ANDROID_API_KEY", "\"$androidApiKey}\"")
        manifestPlaceholders["ANDROID_API_KEY"] = androidApiKey ?: ""

        versionName = "1.0"
        versionCode = 1
    }

    namespace = "io.ashdavies.playground"
}

kotlin {
    androidMain.dependencies {
        implementation(projects.appLauncher.common)
        implementation(projects.httpClient)

        implementation(compose.runtime)

        implementation(libs.androidx.core.splashscreen)
        implementation(libs.google.android.material)
        implementation(libs.ktor.client.core)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.slack.circuit.overlay)
    }

    val androidInstrumentedTest by sourceSets.getting {
        dependsOn(androidMain)
    }
}
