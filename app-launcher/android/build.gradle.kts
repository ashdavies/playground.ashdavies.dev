plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

android {
    val androidTest by sourceSets.getting {
        res.srcDirs("src/androidMain/res")
    }

    defaultConfig {
        versionName = "1.0"
        versionCode = 1
    }

    namespace = "io.ashdavies.playground"
}

buildConfig {
    val androidApiKey by stringProperty(::buildConfigField)
    val androidStrictMode by booleanProperty(::buildConfigField)

    packageName.set(android.namespace)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.appLauncher.common)
            implementation(projects.httpClient)
            implementation(projects.platformScaffold)
            implementation(projects.platformSupport)

            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.google.android.material)
            implementation(libs.ktor.client.core)
            implementation(libs.slack.circuit.foundation)
            implementation(libs.slack.circuit.overlay)
            implementation(libs.squareup.okio)
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.core.splashscreen)
                implementation(libs.google.android.material)
            }
        }
    }
}
