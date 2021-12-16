import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.jetbrainsCompose(): MavenArtifactRepository =
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

fun RepositoryHandler.jitpack(group: String): MavenArtifactRepository =
    maven("https://jitpack.io") {
        content { includeGroup("com.github.$group") }
    }
