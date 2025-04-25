plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.platform.scaffold"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platformSupport)
    }
}
