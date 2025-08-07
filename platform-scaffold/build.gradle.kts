plugins {
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.platform.scaffold"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platformSupport)
    }
}
