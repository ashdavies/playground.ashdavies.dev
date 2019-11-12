buildscript {
  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.0.0-alpha02")
    classpath("com.google.gms:google-services:4.3.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0")
  }
}

allprojects {
  apply(from = "${rootProject.projectDir}/repositories.gradle.kts")
}
