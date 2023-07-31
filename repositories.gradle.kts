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

    resolutionStrategy.eachPlugin {
        when (requested.id.id) {
            "androidx.build.gradle.gcpbuildcache" -> useModule("androidx.build.gradle.gcpbuildcache:gcpbuildcache:${requested.version}")
        }
    }
}
