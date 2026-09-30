plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

compose.resources {
    packageOfResClass = "dev.ashdavies.playground.ui.resources"
    publicResClass = true
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.ui.resources"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.components.resources)
        }
    }
}
