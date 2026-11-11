plugins {
    id("dev.ashdavies.android.application")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.properties")

    alias(libs.plugins.cash.paparazzi)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.google.services)
}

android {
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

            signingConfig = when (isBundle) {
                true -> signingConfigs.findByName("upload")
                false -> signingConfigs.findByName("release")
            }

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    defaultConfig {
        applicationId = "dev.ashdavies.playground"

        manifestPlaceholders["ANDROID_API_KEY"] = "${stringPropertyOrNull("androidApiKey")}"

        versionCode = stringPropertyOrNull("versionCode")?.toIntOrNull() ?: 1
        versionName = stringPropertyOrNull("versionName")?: "0.0.0-SNAPSHOT"
    }

    namespace = "dev.ashdavies.android.playground"
}

dependencies {
    implementation(projects.conferenceApp)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.google.android.material)

    implementation(dependencies.platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.appcheck.playintegrity)
    implementation(libs.google.firebase.perf)

    debugImplementation(libs.google.firebase.appcheck.debug)

    testImplementation(projects.composeMaterial)
    testImplementation(projects.core.ui.resources)
    testImplementation(projects.features.eventDetail)
    testImplementation(projects.features.eventGrid)
    testImplementation(projects.features.eventList)
    testImplementation(projects.features.gallerySync)
    testImplementation(projects.fixtures.events)

    testImplementation(libs.compose.components.resources)
    testImplementation(libs.compose.material3)
    testImplementation(libs.compose.materialIconsExtended)
    testImplementation(libs.kotlinx.datetime)
}

tasks.withType<Test>().configureEach {
    if (name.endsWith("UnitTest")) {
        reports.html.required.set(false)
    }
}
