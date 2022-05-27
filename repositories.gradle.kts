pluginManagement {
    fun RepositoryHandler.maven(url: String, vararg groups: String): MavenArtifactRepository =
        maven(url) { content { groups.forEach(::includeGroup) } }

    fun RepositoryHandler.snapshots(group: String): MavenArtifactRepository =
        maven("https://oss.sonatype.org/content/repositories/snapshots", group)

    fun RepositoryHandler.jetbrainsCompose(): MavenArtifactRepository =
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    fun RepositoryHandler.jitpack(group: String): MavenArtifactRepository =
        maven("https://jitpack.io", "com.github.$group")

    fun RepositoryHandler.default() {
        gradlePluginPortal()
        jetbrainsCompose()
        jitpack("requery")
        mavenCentral()
        google()
    }

    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories.default()
    }

    repositories.default()
}
