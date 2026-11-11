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
            api(libs.compose.uiToolingPreview)

            implementation(projects.composeMaterial)
        }
    }
}
