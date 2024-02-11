import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

buildscript {
    dependencies {
        // noinspection GradleDependency
        classpath("com.squareup.okio:okio:3.8.0")?.because("""
            androidx.build.gradle.gcpbuildcache uses older versions of okio
        """.trimIndent())
    }
}

pluginManagement.repositories {
    includeBuild("build-plugins")
    includeBuild("fused-properties")

    gradlePluginPortal()
    google()
    mavenCentral()
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement.repositories {
    google()
    mavenCentral()
}

plugins {
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0-beta07"
    id("com.google.cloud.tools.jib") version "3.4.0" apply false
    id("com.gradle.enterprise") version "3.16.2"
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
    ":compose-cli",
    ":compose-material",
    ":dominion-app",
    ":events-aggregator",
    ":http-client",
    ":http-common",
    ":identity-manager",
    ":kotlin-gb",
    ":map-routes",
    ":micro-yaml",
    ":notion-console",
    ":nsd-manager",
    ":parcelable-support",
    ":platform-support",
    ":sql-driver",
)

rootProject.name = "playground"
