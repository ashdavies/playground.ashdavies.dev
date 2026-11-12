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
            api(libs.compose.foundation)

            implementation(projects.composeMaterial)
            implementation(projects.core.ui.resources)
            implementation(projects.previews.tooling)

            implementation(libs.compose.materialIconsExtended)
        }
    }
}
