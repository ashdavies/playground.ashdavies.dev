buildscript {
  repositories {
    android()
    gradlePluginPortal()
    kotlin()
    mavenCentral()
    trove4j()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.0.0-alpha09")
    classpath("com.github.ben-manes:gradle-versions-plugin:0.27.0")
    classpath("com.google.gms:google-services:4.3.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70-eap-42")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.0")
  }
}

subprojects {
  repositories {
    android()
    jitpack()
    kotlin()
    mavenCentral()
    trove4j()
  }
}
