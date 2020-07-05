import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

private const val DATA_STORE_SNAPSHOT =
    "https://androidx.dev/snapshots/builds/6639139/artifacts/repository"

private const val JITPACK =
    "https://jitpack.io"

private const val KOTLIN_EAP =
    "https://dl.bintray.com/kotlin/kotlin-eap"

fun RepositoryHandler.android(): MavenArtifactRepository = google {
    content {
        includeGroupByRegex("androidx.*")
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")

        includeModule("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
}

fun RepositoryHandler.jitpack(): MavenArtifactRepository = maven(JITPACK) {
    content {
        includeGroupByRegex("com\\.github\\.ashdavies.*")
    }
}

fun RepositoryHandler.kotlin(): MavenArtifactRepository = maven(KOTLIN_EAP) {
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

fun RepositoryHandler.dataStore(): MavenArtifactRepository = maven(DATA_STORE_SNAPSHOT) {
    content {
        includeGroup("androidx.datastore")
    }
}
