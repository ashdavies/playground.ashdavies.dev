pluginManagement {
    fun RepositoryHandler.default() {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    dependencyResolutionManagement {
        // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories.default()
    }

    repositories.default()
}
