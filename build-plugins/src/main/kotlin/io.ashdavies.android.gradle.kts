plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget()
}

commonExtension {
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
