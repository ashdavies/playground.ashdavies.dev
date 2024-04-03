import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

buildscript {
    dependencies {
        classpath("com.squareup.okio:okio:3.9.0")
            ?.because("androidx.build.gradle.gcpbuildcache uses older versions of okio")

        classpath("org.ow2.asm:asm:9.7")
            ?.because("com.google.cloud.tools.jib uses older versions of asm")
    }
}

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
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0-beta07"
    id("com.google.cloud.tools.jib") version "3.4.2" apply false
    id("com.gradle.enterprise") version "3.17"
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
    ":after-party",
    ":app-check:app-check-client",
    ":app-check:app-check-common",
    ":app-check:app-check-sdk",
    ":app-launcher:android",
    ":app-launcher:common",
    ":app-launcher:desktop",
    ":circuit-support",
    ":cloud-firestore",
    ":cloud-run",
    ":compose-material",
    ":dominion-app",
    ":events-aggregator",
    ":http-client",
    ":http-common",
    ":identity-manager",
    ":kotlin-gb",
    ":map-routes",
    ":maps-routing",
    ":micro-yaml",
    ":nsd-manager",
    ":parcelable-support",
    ":platform-scaffold",
    ":platform-support",
    ":sql-driver",
)

rootProject.name = "playground"
