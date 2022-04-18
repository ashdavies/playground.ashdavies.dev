dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

include(
    ":android",
    ":auth-oauth",
    ":cloud-aggregator",
    ":cloud-functions",
    ":compose-local",
    ":dominion",
    ":local-storage",
    ":notion-console",
    ":version-catalog"
)

includeBuild("build-plugins")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.9"
}


rootProject.name = "playground"
