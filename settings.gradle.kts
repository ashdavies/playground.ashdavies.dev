import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

pluginManagement.repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement.repositories {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    google()
    mavenCentral()
}

plugins {
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0-beta04"
    id("com.google.cloud.tools.jib") version "3.4.0" apply false
    id("com.gradle.enterprise") version "3.15"
}

buildCache {
    registerBuildCacheService(GcpBuildCache::class, GcpBuildCacheServiceFactory::class)

    remote(GcpBuildCache::class) {
        bucketName = "playground-build-cache"
        projectId = "playground-1a136"
        isPush = true
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

include(
    ":app-check:app-check-client",
    ":app-check:app-check-common",
    ":app-check:app-check-sdk",
    ":app-launcher:android",
    ":app-launcher:common",
    ":app-launcher:desktop",
    ":cloud-firestore",
    ":cloud-run",
    ":compose-cli",
    ":dominion-app",
    ":events-aggregator",
    ":events-app",
    ":firebase-compose",
    ":gallery-app",
    ":http-client",
    ":image-loader",
    ":kotlin-gb",
    ":local-storage",
    ":micro-yaml",
    ":notion-client",
    ":nsd-manager",
    ":notion-console",
    ":parcelable-support",
    ":platform-support",
    ":sql-driver",
)

includeBuild("build-plugins")

rootProject.name = "playground"
