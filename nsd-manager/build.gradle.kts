plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

android {
    namespace = "dev.ashdavies.nsd"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.annotation)
        }
    }
}
