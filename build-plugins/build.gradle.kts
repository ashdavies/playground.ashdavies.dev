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
        create("multiplatform-library") {
            implementationClass = "MultiplatformLibrary"
            id = "multiplatform-library"
        }

        create("multiplatform-sql") {
            implementationClass = "MultiplatformSql"
            id = "multiplatform-sql"
        }
    }
}
