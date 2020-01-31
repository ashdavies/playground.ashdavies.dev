<<<<<<< Updated upstream
include(":mobile", ":mobile-ktx")
=======
include(":mobile", ":mobile-ktx")

pluginManagement {
  plugins {
    Kotlin(
        kotlin("android.extensions"),
    )
    Android.Plugin(
        id("com.android.application"),
        id("com.android.library")
    )
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id.startsWith("com.android")) {
        useModule("com.android.tools.build:gradle:${target.version ?: requested.version}")
      }
    }
  }
}

repositories {
  google {
    content {
      includeGroupByRegex("androidx.*")
      includeGroupByRegex("com\\.android.*")
      includeGroupByRegex("com\\.google.*")
      includeGroupByRegex("zipflinger.*")
    }
  }

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

  maven("https://jitpack.io") {
    content {
      includeGroupByRegex("com\\.github\\.ashdavies.*")
    }
  }

  mavenCentral()
}
>>>>>>> Stashed changes
