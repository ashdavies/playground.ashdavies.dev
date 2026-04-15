import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    id("dev.ashdavies.android.application")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.properties")

    alias(libs.plugins.cash.paparazzi)
}

android {
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            val keystoreProperties = localPropertyProvider("keystore.properties")

            signingConfig = keystoreProperties.map { properties ->
                signingConfigs.maybeCreate("release").apply {
                    storeFile = file(properties.getProperty("store.file"))
                    storePassword = properties.getProperty("store.password")

                    keyAlias = properties.getProperty("key.alias")
                    keyPassword = properties.getProperty("key.password")

                    enableV3Signing = true
                    enableV4Signing = true
                }
            }.orNull

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    defaultConfig {
        val androidApiKey by stringProperty { _, value ->
            manifestPlaceholders["ANDROID_API_KEY"] = "$value"
        }

        val versionCode by stringProperty { _, value ->
            versionCode = value?.toInt() ?: 1
        }

        val versionName by stringProperty { _, value ->
            versionName = value ?: "0.0.0-SNAPSHOT"
        }
    }

    namespace = "dev.ashdavies.playground"
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

dependencies {
    implementation(projects.conferenceApp)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.android.location)
    implementation(libs.google.android.material)
    implementation(libs.google.maps.android.compose)
    implementation(libs.google.maps.android.utils)

    implementation(dependencies.platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.appcheck.playintegrity)
    implementation(libs.google.firebase.crashlytics)

    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.slack.circuit.overlay)

    debugImplementation(libs.google.firebase.appcheck.debug)
    debugImplementation(libs.compose.uiTooling)
}
