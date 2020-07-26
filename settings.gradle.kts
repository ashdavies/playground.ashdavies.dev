pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = ("playground")

include(
    ":mobile",
    ":mobile-ktx"
)
