import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.jetbrainsCompose(): MavenArtifactRepository =
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

fun RepositoryHandler.jitpack(group: String): MavenArtifactRepository =
    maven("https://jitpack.io", "com.github.$group")

fun RepositoryHandler.kotlinEap(): MavenArtifactRepository =
    maven("https://dl.bintray.com/kotlin/kotlin-eap")

fun RepositoryHandler.snapshots(group: String): MavenArtifactRepository =
    maven("https://oss.sonatype.org/content/repositories/snapshots", group)

private fun RepositoryHandler.maven(url: String, vararg groups: String): MavenArtifactRepository =
    maven(url) { content { groups.forEach(::includeGroup) } }
