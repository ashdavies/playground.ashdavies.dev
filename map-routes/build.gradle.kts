plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

android {
    defaultConfig {
        val androidApiKey by stringProperty { _, value ->
            manifestPlaceholders["ANDROID_API_KEY"] = "$value"
        }
    }

    namespace = "io.ashdavies.routes"
}

buildConfig {
    val androidApiKey by stringProperty(::buildConfigField)

    packageName.set(android.namespace)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.mapsRouting)
            implementation(projects.platformSupport)

            implementation(compose.material3)

            implementation(libs.androidx.annotation)
            implementation(libs.ktor.client.core)
            implementation(libs.slack.circuit.foundation)
        }

        androidMain.dependencies {
            implementation(libs.google.accompanist.permissions)
            implementation(libs.google.android.location)
            implementation(libs.google.maps.android.compose)
            implementation(libs.google.maps.android.utils)
            implementation(libs.kotlinx.coroutines.play.services)
        }
    }
}
