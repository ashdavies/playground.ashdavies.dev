import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

buildscript {
    dependencies {
        classpath("com.squareup.okio:okio:3.9.1")
            ?.because("androidx.build.gradle.gcpbuildcache uses older versions of okio")

        classpath("org.ow2.asm:asm:9.7.1")
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
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0"
    id("com.google.cloud.tools.jib") version "3.4.4" apply false
    id("com.gradle.develocity") version "3.18.2"
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
    ":app-launcher:android",
    ":app-launcher:common",
    ":app-launcher:desktop",
    ":asg-service",
    ":cloud-run",
    ":compose-material",
    ":conferences-app",
    ":http-client",
    ":http-common",
    ":identity-manager",
    ":key-navigation",
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
)

rootProject.name = "playground"
