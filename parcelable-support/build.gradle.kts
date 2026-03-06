plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    id("kotlin-parcelize")
}

android {
    namespace = "dev.ashdavies.parcelable"
}
