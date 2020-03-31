import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.android(): MavenArtifactRepository = google {
  content {
    includeGroupByRegex("androidx.*")
    includeGroupByRegex("com\\.android.*")
    includeGroupByRegex("com\\.google.*")

    includeModule("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
  }
}

fun RepositoryHandler.jitpack(): MavenArtifactRepository = maven("https://jitpack.io") {
  content {
    includeGroupByRegex("com\\.github\\.ashdavies.*")
  }
}

fun RepositoryHandler.kotlin(): MavenArtifactRepository = maven("https://dl.bintray.com/kotlin/kotlin-eap") {
  content {
    includeGroup("org.jetbrains.kotlin")
  }
}

fun RepositoryHandler.tensorflow(): MavenArtifactRepository = jcenter {
  content {
    includeModule("org.tensorflow", "tensorflow-lite-support")
  }
}

fun RepositoryHandler.trove4j(): MavenArtifactRepository = jcenter {
  content {
    includeModule("org.jetbrains.trove4j", "trove4j")
  }
}
