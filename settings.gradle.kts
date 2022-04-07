dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "playground"

include(
    ":android",
    ":auth-oauth",
    ":cloud-aggregator",
    ":cloud-functions",
    ":compose-local",
    ":local-storage",
    ":notion-console",
    ":version-catalog"
)

includeBuild("build-plugins")

enableFeaturePreview("VERSION_CATALOGS")
