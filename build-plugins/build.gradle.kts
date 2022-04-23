plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.tools.build.gradle)
    implementation(libs.jetbrains.compose.gradle.plugin)
    implementation(libs.jetbrains.kotlin.gradle.plugin)
    implementation(libs.jetbrains.kotlin.serialization.gradlePlugin)
    implementation(libs.sqldelight.kotlin.gradle.plugin)
}
