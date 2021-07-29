import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.kotlinx(): MavenArtifactRepository = maven("https://kotlin.bintray.com/kotlinx/")
