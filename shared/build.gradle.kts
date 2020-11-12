plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.10"

    id("com.android.library")
    id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(30)

    sourceSets.forEach { sourceSet ->
        sourceSet
            .manifest
            .srcFile("src/androidMain/AndroidManifest.xml")
    }

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
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
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(ProjectDependencies.AndroidX.coreKtx)
                implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesAndroid)
                implementation(ProjectDependencies.Ktor.ktorClientAndroid)
                implementation(ProjectDependencies.Square.SqlDelight.androidDriver)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(ProjectDependencies.JetBrains.Kotlin.kotlinTest)
                implementation(ProjectDependencies.JetBrains.Kotlin.kotlinTestJunit)
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxDatetime)
                implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesCore)
                implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxSerializationJson)
                implementation(ProjectDependencies.Ktor.ktorClientCore)
                implementation(ProjectDependencies.Ktor.ktorClientJson)
                implementation(ProjectDependencies.Ktor.ktorClientLogging)
                implementation(ProjectDependencies.Ktor.ktorClientSerialization)
                implementation(ProjectDependencies.Square.SqlDelight.coroutinesExtensions)
                implementation(ProjectDependencies.Square.SqlDelight.runtime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(ProjectDependencies.JetBrains.Kotlin.kotlinTestCommon)
                implementation(ProjectDependencies.JetBrains.Kotlin.kotlinTestAnnotationsCommon)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(ProjectDependencies.Ktor.ktorClientIos)
                implementation(ProjectDependencies.Square.SqlDelight.nativeDriver)
            }
        }
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground.database"
    }
}
