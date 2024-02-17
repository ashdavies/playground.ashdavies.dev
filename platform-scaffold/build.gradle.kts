plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.platform.scaffold"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)
    }
}
