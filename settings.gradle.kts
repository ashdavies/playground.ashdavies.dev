apply(from = "repositories.gradle.kts")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":app-check:app-check-client",
    ":app-check:app-check-compose",
    ":app-check:app-check-function",
    ":app-check:app-check-sdk",
    ":app-launcher:android",
    ":app-launcher:common",
    ":app-launcher:desktop",
    ":auth-oauth",
    ":cloud-functions",
    ":cloud-run",
    ":compose-constructor:plugin-common",
    // ":compose-constructor:plugin-ide",
    // ":compose-constructor:plugin-native",
    ":compose-constructor:plugin-runtime",
    ":compose-locals",
    ":dominion-app",
    ":events-aggregator",
    ":events-app",
    ":events-function",
    ":firebase-compose",
    ":google-cloud",
    ":local-remote",
    ":local-storage",
    ":notion-console",
    ":paging-compose",
    ":playground-app",
    ":version-catalog",
)

includeBuild("build-plugins")

includeBuild("compose-constructor/plugin-gradle") {
    dependencySubstitution {
        substitute(module("io.ashdavies.playground:plugin-gradle")).using(project(":"))
    }
}

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
