plugins {
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
