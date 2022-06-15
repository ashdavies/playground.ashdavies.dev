apply(from = "repositories.gradle.kts")

include(
    ":app-check",
    ":auth-oauth",
    ":cloud-functions",
    ":events-aggregator",
    ":events-app",
    ":events-function",
    ":dominion-app",
    ":local-remote",
    ":local-storage",
    ":notion-console",
    ":playground-app",
    ":version-catalog",
)

includeBuild("build-plugins")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.9"
}

rootProject.name = "playground"
