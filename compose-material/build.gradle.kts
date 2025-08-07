plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.material"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.material3)
    }
}
