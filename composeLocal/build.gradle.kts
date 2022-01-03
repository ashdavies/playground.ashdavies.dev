// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("compose-multiplatform")
    id(libs.plugins.android.library)
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    configurations {
        // https://youtrack.jetbrains.com/issue/KT-43944
        create("testApi", "testDebugApi", "testReleaseApi")
    }

    defaultConfig {
        compileSdk = 31
    }

    sourceSets.forEach { sourceSet ->
        sourceSet.manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

kotlin {
    android()
    jvm()
}
