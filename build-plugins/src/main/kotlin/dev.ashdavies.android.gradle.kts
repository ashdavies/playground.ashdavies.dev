import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget {
        compilerOptions.jvmTarget = libs.versions.kotlin.jvmTarget
            .map(JvmTarget::fromTarget)
            .get()
    }
}

project.commonExtension {
    compileOptions {
        sourceCompatibility = libs.versions.kotlin.jvmTarget
            .map(JavaVersion::toVersion)
            .get()

        targetCompatibility = libs.versions.kotlin.jvmTarget
            .map(JavaVersion::toVersion)
            .get()
    }

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk
            .map(String::toInt)
            .get()

        minSdk = libs.versions.android.minSdk
            .map(String::toInt)
            .get()
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
