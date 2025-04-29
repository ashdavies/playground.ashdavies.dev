import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

buildscript {
    dependencies {
        classpath("org.apache.commons:commons-compress:1.27.1")?.because(
            """
                android.application depends upon org.apache.commons:commons-compress:1.21
                https://github.com/GoogleContainerTools/jib/issues/4235
            """.trimIndent(),
        )

        classpath("com.google.http-client:google-http-client:1.47.0")?.because(
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
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0"
    id("com.gradle.develocity") version "4.0.1"
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
    ":app-check",
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
    ":sql-driver",
    ":tally-app",
)

rootProject.name = "playground"
