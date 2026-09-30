plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android {
        namespace = "dev.ashdavies.playground.preview.tooling"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeMaterial)

            implementation(libs.compose.material3)
            implementation(libs.compose.uiToolingPreview)
        }
    }
}
