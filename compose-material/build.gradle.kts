plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.material"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.material3)
    }
}
