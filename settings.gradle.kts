apply(from = "repositories.gradle.kts")

include(
    ":app-check",
    ":auth-oauth",
    ":cloud-functions",
    ":compose-constructor:plugin-common",
    // ":compose-constructor:plugin-ide",
    // ":compose-constructor:plugin-native",
    ":compose-constructor:plugin-runtime",
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

includeBuild("app-manifest")
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
