plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.nsd"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)
    }
}
