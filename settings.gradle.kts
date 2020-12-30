pluginManagement {
    repositories {
        google()
        maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
        maven("https://kotlin.bintray.com/kotlinx/")
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "playground"

include(
    ":androidApp",
    ":functions",
    ":shared"
)
