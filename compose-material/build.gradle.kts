plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.material"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.material3)
    }
}
