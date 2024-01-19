import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget()
}

extensions.withType<BaseAppModuleExtension> { configure() }

extensions.withType<LibraryExtension> { configure() }

fun CommonExtension<*, *, *, *, *>.configure() {
    buildFeatures {
        compose = true
    }

    composeOptions {
        val composeCompilerVersion = libs.versions.compose.compiler.get()
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }

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
