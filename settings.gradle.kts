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
    ":androidApp",
    ":cloudAggregator",
    ":cloudFunctions",
    ":commonApp",
    ":composeLocal",
    ":localStorage",
    ":notionConsole",
    ":versionCatalog"
)

enableFeaturePreview("VERSION_CATALOGS")
