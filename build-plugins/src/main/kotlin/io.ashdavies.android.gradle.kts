import org.jetbrains.kotlin.gradle.dsl.JvmTarget

private val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
    }
}

project.commonExtension {
    compileOptions {
        sourceCompatibility(jvmTargetVersion)
        targetCompatibility(jvmTargetVersion)
    }

    defaultConfig {
        val compileSdkVersion = libs.versions.android.compileSdk.get()
        compileSdk = compileSdkVersion.toInt()

        val minSdkVersion = libs.versions.android.minSdk.get()
        minSdk = minSdkVersion.toInt()
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
