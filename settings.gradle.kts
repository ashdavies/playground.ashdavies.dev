apply(from = "repositories.gradle.kts")

include(
    ":app-check:app-check-client",
    ":app-check:app-check-compose",
    ":app-check:app-check-function",
    ":app-check:app-check-sdk",
    ":auth-oauth",
    ":cloud-functions",
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
    ":local-remote",
    ":local-storage",
    ":notion-console",
    ":playground-app",
    ":version-catalog",
)

includeBuild("build-plugins")
includeBuild("cloud-deploy")

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
