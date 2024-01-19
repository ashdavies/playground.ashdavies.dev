plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.platform"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)
    }
}
