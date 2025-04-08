import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

buildscript {
    dependencies {
        classpath("com.google.cloud.tools:jib-core:0.27.3")?.because(
            """
                android.application depends upon org.apache.commons:commons-compress:1.21
                gcpbuildcache depends upon org.apache.httpcomponents:httpclient:4.5.14
                @see https://github.com/GoogleContainerTools/jib/issues/4235
            """.trimIndent()
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
