plugins {
    kotlin("multiplatform")

    id("com.android.library")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(30)

    sourceSets["main"]
        .manifest
        .srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        setMinSdkVersion(24)
        setTargetSdkVersion(30)
    }
}

kotlin {
    android()

    ios {
        binaries {
            framework {
                baseName = "conferences"
            }
        }
    }

    sourceSets {
        val commonMain by getting

        val androidMain by getting {
            dependencies {
                implementation(ProjectDependencies.AndroidX.coreKtx)
            }
        }

        val iosMain by getting
    }
}
