buildscript {
  repositories {
    android
    kotlin
    trove4j

    mavenCentral()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.0.0-alpha05")
    classpath("com.google.gms:google-services:4.3.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0")
  }
}

subprojects {
  repositories {
    android
    kotlin
    trove4j

    jitpack("ashdavies")
    mavenCentral()
  }
}
