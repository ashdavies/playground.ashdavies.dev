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
    ":consoleApp",
    ":functions",
    ":notionClient",
    ":shared",
    ":sqlDriver",
    ":uuidRegistry",
    ":versionCatalog"
)

enableFeaturePreview("VERSION_CATALOGS")
