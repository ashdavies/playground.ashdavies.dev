plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")
    id("io.ashdavies.spotless")

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

    packaging {
        resources.excludes += setOf(
            "META-INF/DEPENDENCIES", // com.google.maps:google-maps-routing
            "META-INF/INDEX.LIST", // com.google.maps:google-maps-routing
        )
    }

    namespace = "io.ashdavies.playground"
}

buildConfig {
    val androidApiKey by stringProperty { value ->
        buildConfigField("ANDROID_API_KEY", value)
    }

    packageName.set(android.namespace)
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)
    }

    androidMain.dependencies {
        implementation(projects.appLauncher.common)
        implementation(projects.httpClient)
        implementation(projects.platformSupport)

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
