plugins {
  `kotlin-dsl`
}

repositories {
  google {
    content {
      includeGroupByRegex("androidx.*")
      includeGroupByRegex("com\\.android.*")
      includeGroupByRegex("com\\.google.*")
    }
  }

  maven("https://dl.bintray.com/kotlin/kotlin-eap") {
    content {
      includeGroup("org.jetbrains.kotlin")
    }
  }

  jcenter {
    content {
      includeModule("eu.appcom.gradle", "android-versioning")
      includeModule("org.jetbrains.trove4j", "trove4j")
      includeModule("org.tensorflow", "tensorflow-lite-support")
    }
  }

  mavenCentral()
}

dependencies {
  implementation("com.android.tools.build:gradle:4.1.0-alpha04")
  implementation("eu.appcom.gradle:android-versioning:1.0.2")
}
