plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

android {
    namespace = "dev.ashdavies.config"
}

kotlin {
    sourceSets.androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(libs.google.firebase.config)
    }
}
