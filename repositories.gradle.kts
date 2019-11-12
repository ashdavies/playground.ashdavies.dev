repositories {
  google() {
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
