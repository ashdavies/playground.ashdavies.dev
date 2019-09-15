buildscript {
  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:3.6.0-alpha10")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0")
  }
}

allprojects {
  apply(from = "${rootProject.projectDir}/repositories.gradle.kts")
}
