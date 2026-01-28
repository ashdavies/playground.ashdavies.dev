plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.placeholder"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.androidx.annotation)
        implementation(libs.compose.material3)
    }
}
