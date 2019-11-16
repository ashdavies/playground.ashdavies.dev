apply(from = "${rootProject.projectDir}/../repositories.gradle.kts")

plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  google()
}

dependencies {
  implementation("com.android.tools.build:gradle:4.0.0-alpha02")
}
