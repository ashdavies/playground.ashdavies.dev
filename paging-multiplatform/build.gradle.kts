plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android.namespace = "dev.ashdavies.paging"

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.paging.common)
            api(libs.androidx.paging.compose)

            implementation(libs.compose.foundation)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
