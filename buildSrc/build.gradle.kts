plugins {
  `kotlin-dsl`
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

  jcenter {
    content {
      includeGroup("org.jetbrains.trove4j")
    }
  }

  mavenCentral()
}

dependencies {
  implementation("com.android.tools.build:gradle:4.0.0-alpha09")
}
