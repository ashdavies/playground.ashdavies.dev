plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.ui.snackbar"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.material3)
            api(libs.kotlinx.coroutines.core)
        }
    }
}

metro {
    generateContributionProviders = true
}
