dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

include(
    ":android",
    ":auth-oauth",
    ":cloud-aggregator",
    ":cloud-functions",
    ":dominion-app",
    ":local-storage",
    ":notion-console",
    ":playground-app",
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
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.9"
}


rootProject.name = "playground"
