plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

compose.resources {
    packageOfResClass = "dev.ashdavies.playground.ui"
    publicResClass = true
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.ui"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeMaterial)

            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.datetime)
        }
    }
}
