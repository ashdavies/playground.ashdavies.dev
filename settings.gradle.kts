import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

buildscript {
    dependencies {
        classpath("com.google.http-client:google-http-client:2.1.1")?.because(
            "gcpbuildcache depends upon org.apache.httpcomponents:httpclient:4.5.14",
        )
    }
}

pluginManagement.repositories {
    includeBuild("build-plugins")
    includeBuild("cloud-build")
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
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("androidx.build.gradle.gcpbuildcache") version "1.0.1"
    id("com.gradle.develocity") version "4.5.0"
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
    ":android-app",
    ":app-check",
    ":asg-service",
    ":cloud-common",
    ":cloud-run",
    ":compose-material",
    ":conference-app",
    ":feature:event-common",
    ":feature:event-detail",
    ":feature:event-grid",
    ":feature:event-list",
    ":feature:gallery-sync",
    ":feature:pager-factory",
    ":http-client",
    ":http-common",
    ":identity-manager",
    ":key-navigation",
    ":kotlin-gb",
    ":maps-routing",
    ":parcelable-support",
    ":platform-support",
    ":remote-config",
    ":sql-common",
    ":sql-driver",
    ":ui-components",
)

rootProject.name = "playground"
