import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget {
        val jvmTarget = JvmTarget.fromTarget(libs.versions.kotlin.jvmTarget.get())
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(jvmTarget)
    }
}

pluginManager.commonExtension {
    compileOptions {
        val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()
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
