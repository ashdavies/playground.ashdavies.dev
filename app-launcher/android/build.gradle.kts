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
    commonMain.dependencies {
        implementation(compose.material)
    }

    val androidInstrumentedTest by sourceSets.getting {
        dependsOn(androidMain)
    }
}
