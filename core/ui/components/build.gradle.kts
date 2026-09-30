plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.ui"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeMaterial)
            implementation(projects.core.ui.resources)
            implementation(projects.previews.tooling)

            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.datetime)
        }
    }
}
