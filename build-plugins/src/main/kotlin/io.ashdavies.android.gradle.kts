import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget()

    androidMain.dependencies {
        implementation(libs.androidx.annotation)
        implementation(libs.androidx.core.ktx)
    }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        compileSdk = Playground.compileSdk
        minSdk = Playground.minSdk
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
