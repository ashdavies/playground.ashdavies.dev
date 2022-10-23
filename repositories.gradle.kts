pluginManagement {
    fun RepositoryHandler.default() {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
