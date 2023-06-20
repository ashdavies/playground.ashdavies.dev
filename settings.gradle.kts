import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

apply(from = "repositories.gradle.kts")

plugins {
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0-beta01"
    id("com.google.cloud.tools.jib") version "3.3.2" apply false
    id("com.gradle.enterprise") version "3.13.4"
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
    ":cloud-backend:cloud-firestore",
    ":cloud-backend:cloud-run",
    ":cloud-backend:google-cloud",
    ":cloud-backend:http-client",
    ":cloud-backend:micro-yaml",
    ":compose-locals",
    ":dominion-app",
    ":events-aggregator",
    ":events-app",
    ":firebase-compose",
    ":local-storage",
    ":notion-client",
    ":notion-console",
    ":parcelable-support",
    ":playground-app",
    ":ratings-app",
    ":sql-driver",
)

includeBuild("build-plugins")

rootProject.name = "playground"
