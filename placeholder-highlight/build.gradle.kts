plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.placeholder"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.material3)

        implementation(libs.androidx.annotation)
    }
}
