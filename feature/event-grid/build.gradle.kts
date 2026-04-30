plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

android {
    namespace = "dev.ashdavies.event.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.eventCommon)
        }
    }
}
