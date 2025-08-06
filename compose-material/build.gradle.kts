plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.default")
}

android {
    namespace = "io.ashdavies.material"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.material3)
    }
}
