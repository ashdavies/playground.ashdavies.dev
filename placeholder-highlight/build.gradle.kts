plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.androidx.annotation)
        implementation(libs.compose.material3)
    }
}
