@file:Suppress("UnstableApiUsage")

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.jetbrains.compose.gradlePlugin)
    implementation(libs.jetbrains.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        // create<ComposeMultiplatform()

        create("compose-multiplatform") {
            implementationClass = "ComposeMultiplatform"
            id = "compose-multiplatform"
        }
    }
}
