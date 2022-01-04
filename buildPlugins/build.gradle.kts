@file:Suppress("UnstableApiUsage")

plugins {
    `java-library`
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.gradlePlugin)
    implementation(libs.jetbrains.compose.gradlePlugin)
    implementation(libs.jetbrains.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        // create<AndroidApplication>()

        create("playground-android-application") {
            implementationClass = "AndroidApplication"
            id = "playground-android-application"
        }

        create("playground-android-library") {
            implementationClass = "AndroidLibrary"
            id = "playground-android-library"
        }

        create("playground-compose-multiplatform") {
            implementationClass = "ComposeMultiplatform"
            id = "playground-compose-multiplatform"
        }
    }
}
