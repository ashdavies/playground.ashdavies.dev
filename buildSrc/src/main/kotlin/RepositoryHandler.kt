import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

val RepositoryHandler.android: MavenArtifactRepository
  get() = google {
    content {
      includeGroupByRegex("androidx.*")
      includeGroupByRegex("com\\.android.*")
      includeGroupByRegex("com\\.google.*")
      includeGroupByRegex("zipflinger.*")
    }
  }

val RepositoryHandler.kotlin: MavenArtifactRepository
  get() = maven("https://dl.bintray.com/kotlin/kotlin-eap") {
    content {
      includeGroup("org.jetbrains.kotlin")
    }
  }

val RepositoryHandler.trove4j: MavenArtifactRepository
  get() = jcenter {
    content {
      includeGroup("org.jetbrains.trove4j")
    }
  }

fun RepositoryHandler.jitpack(name: String): MavenArtifactRepository {
  return maven("https://jitpack.io") {
    content {
      includeGroupByRegex("com\\.github\\.$name.*")
    }
  }
}
