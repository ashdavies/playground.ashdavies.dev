import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

private val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

plugins {
    kotlin("multiplatform")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("androidJvm") {
                withAndroidTarget()
                withJvm()
            }
        }
    }

    androidTarget {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
    }
}

private val androidApplicationPlugin = libs.plugins.android.application.get()
pluginManager.withPlugin(androidApplicationPlugin.pluginId) {
    extensions.configure<BaseAppModuleExtension> {
        compileOptions(jvmTargetVersion)
        defaultConfig()
    }
}

private val androidLibraryPlugin = libs.plugins.android.library.get()
pluginManager.withPlugin(androidLibraryPlugin.pluginId) {
    extensions.configure<LibraryExtension> {
        compileOptions(jvmTargetVersion)
        defaultConfig()
    }
}

private fun CommonExtension<*, *, *, *, *, *>.compileOptions(version: String) {
    compileOptions {
        sourceCompatibility(version)
        targetCompatibility(version)
    }
}

private fun CommonExtension<*, *, *, *, *, *>.defaultConfig() {
    defaultConfig {
        val compileSdkVersion = libs.versions.android.compileSdk.get()
        compileSdk = compileSdkVersion.toInt()

        val minSdkVersion = libs.versions.android.minSdk.get()
        minSdk = minSdkVersion.toInt()
    }
}
