plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android.namespace = "dev.ashdavies.nsd"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.annotation)
        }
    }
}
