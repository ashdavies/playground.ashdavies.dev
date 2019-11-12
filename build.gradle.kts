buildscript {
  repositories {
    // TODO apply(from = "repositories.gradle.kts")

    maven("https://dl.bintray.com/kotlin/kotlin-eap") {
      content {
        includeGroup("org.jetbrains.kotlin")
      }
    }

    maven("https://jcenter.bintray.com") {
      content {
        includeGroup("org.jetbrains.trove4j")
      }
    }

    google()
    mavenCentral()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.0.0-alpha02")
    classpath("com.google.gms:google-services:4.3.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.60-eap-143")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0")
  }
}

allprojects {
  apply(from = "${rootProject.projectDir}/repositories.gradle.kts")
}
