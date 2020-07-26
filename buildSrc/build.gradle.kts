@file:Suppress("UnstableApiUsage")

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.2.0-alpha05")
    implementation("eu.appcom.gradle:android-versioning:1.0.2")
}
