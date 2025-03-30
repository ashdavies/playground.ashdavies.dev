import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

pluginManagement.repositories {
    includeBuild("build-plugins")
    includeBuild("fused-properties")

    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencyResolutionManagement.repositories {
    google()
    mavenCentral()
}

plugins {
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0"
    id("com.gradle.develocity") version "3.19.2"
}

buildCache {
    registerBuildCacheService(GcpBuildCache::class, GcpBuildCacheServiceFactory::class)

    remote(GcpBuildCache::class) {
        bucketName = "playground-build-cache"
        projectId = "playground-1a136"
        isPush = true
    }
}

develocity.buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    termsOfUseAgree = "yes"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":analytics",
    ":app-check:app-check-client",
    ":app-check:app-check-common",
    ":app-check:app-check-sdk",
    ":asg-service",
    ":cloud-run",
    ":composeApp",
    ":compose-material",
    ":http-client",
    ":http-common",
    ":identity-manager",
    ":key-navigation",
    ":kotlin-delegates",
    ":kotlin-gb",
    ":map-routes",
    ":maps-routing",
    ":nsd-manager",
    ":paging-compose",
    ":parcelable-support",
    ":placeholder-highlight",
    ":platform-scaffold",
    ":platform-support",
    ":remote-config",
    ":sql-common",
    ":sql-compose",
    ":sql-driver",
    ":tally-app",
)

rootProject.name = "playground"
