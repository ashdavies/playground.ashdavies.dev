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

            val isBundle = gradle.startParameter.taskNames.any {
                it.contains("bundle", ignoreCase = true)
            }

<<<<<<< Updated upstream
                signingConfig = signingConfigs.maybeCreate("release").apply {
                    storeFile = file(keyStoreProperties.getProperty("store.file"))
                    storePassword = keyStoreProperties.getProperty("store.password")

                    keyAlias = keyStoreProperties.getProperty("key.alias")
                    keyPassword = keyStoreProperties.getProperty("key.password")

                    enableV3Signing = true
                    enableV4Signing = true
                }
=======
            signingConfig = when (isBundle) {
                true -> signingConfigs.findByName("upload")
                false -> signingConfigs.findByName("release")
>>>>>>> Stashed changes
            }

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    defaultConfig {
        val androidApiKey by stringPropertyOrNull { _, value ->
            manifestPlaceholders["ANDROID_API_KEY"] = "$value"
        }

        val versionCode by stringPropertyOrNull { _, value ->
            versionCode = value?.toInt() ?: 1
        }

        val versionName by stringPropertyOrNull { _, value ->
            versionName = value ?: "0.0.0-SNAPSHOT"
        }

        applicationId = "dev.ashdavies.playground"
    }

    namespace = "dev.ashdavies.android.playground"

    val keystoreFile = file("android.keystore")
    if (keystoreFile.exists()) {
        signingConfigs {
            val keystorePassword = stringProperty("keystore.password")

            fun create(name: String) = create(name) {
                storeFile = keystoreFile
                storePassword = keystorePassword
                keyAlias = name
                keyPassword = keystorePassword
            }

            create("release")
            create("upload")
        }
    }
}

dependencies {
    implementation(projects.conferenceApp)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.circuit.overlay)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.android.location)
    implementation(libs.google.android.material)
    implementation(libs.google.maps.android.compose)
    implementation(libs.google.maps.android.utils)

    implementation(dependencies.platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.appcheck.playintegrity)
    implementation(libs.google.firebase.crashlytics)

    implementation(libs.kotlinx.coroutines.play.services)

    debugImplementation(libs.google.firebase.appcheck.debug)
    debugImplementation(libs.compose.uiTooling)

    testImplementation(projects.asgService)
    testImplementation(projects.feature.eventCommon)
    testImplementation(projects.feature.eventDetail)
    testImplementation(projects.feature.eventGrid)
    testImplementation(projects.feature.eventList)
    testImplementation(projects.feature.gallerySync)

    testImplementation(libs.compose.components.resources)
    testImplementation(libs.compose.material3)
    testImplementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlinx.serialization.json)
}

tasks.withType<Test>().configureEach {
    if (name.endsWith("UnitTest")) {
        reports.html.required.set(false)
    }
}
